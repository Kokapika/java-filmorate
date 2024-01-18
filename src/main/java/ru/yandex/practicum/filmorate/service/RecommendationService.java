package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLikes;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final UserService userService;
    private final FilmService filmService;
    private final LikeService likeService;

    public List<Film> getRecommendations(int userId) {
        if (!userService.isExistingUser(userId)) {
            throw new NotFoundException(String.format("Пользователь с id %d не найден", userId));
        }
        List<FilmLikes> mostCommonLikes = getMostCommonLikes(userId);
        List<Integer> filmIds = mostCommonLikes.stream()
                .map(FilmLikes::getFilmId)
                .collect(Collectors.toList());

        return filmService.getFilms(filmIds);
    }

    private List<FilmLikes> getMostCommonLikes(int targetUserId) {
        List<FilmLikes> commonLikes = likeService.getCommonLikes(targetUserId);

        Map<Integer, List<FilmLikes>> commonLikesGroupedByUser = commonLikes.stream()
                .collect(Collectors.groupingBy(FilmLikes::getUserId));
        List<FilmLikes> likesByTargetUser = commonLikesGroupedByUser.remove(targetUserId);
        if (likesByTargetUser == null) {
            return List.of();
        }

        Map<List<FilmLikes>, Integer> intersectionOccasion = commonLikesGroupedByUser.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, v -> 0));
        for (FilmLikes likeByTargetUser : likesByTargetUser) {
            intersectionOccasion.forEach((likesListKey, occasion) -> {
                if (likesListKey.stream().anyMatch(f -> f.getFilmId() == likeByTargetUser.getFilmId())) {
                    intersectionOccasion.put(likesListKey, occasion + 1);
                }
            });
        }
        if (intersectionOccasion.isEmpty()) {
            return List.of();
        }

        List<FilmLikes> maxOccasionEntry = Collections.max(intersectionOccasion.entrySet(), Map.Entry.comparingByValue()).getKey();
        List<FilmLikes> mostCommonLikes = maxOccasionEntry.stream()
                .filter(s -> likesByTargetUser.stream()
                .noneMatch(f -> f.getFilmId() == s.getFilmId()))
                .collect(Collectors.toList());
        return mostCommonLikes;
    }
}