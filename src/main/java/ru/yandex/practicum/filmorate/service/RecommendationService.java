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
        return getMostCommonLikes(userId).stream()
                .map(filmLikes -> filmService.getFilmById(filmLikes.getFilmId()))
                .collect(Collectors.toList());
    }

    private List<FilmLikes> getMostCommonLikes(int targetUserId) {
        List<FilmLikes> allLikes = likeService.getAllLikes();

        Map<Integer, List<FilmLikes>> allLikesGroupedByUser = allLikes.stream()
                .collect(Collectors.groupingBy(FilmLikes::getUserId));
        List<FilmLikes> likesByTargetUser = allLikesGroupedByUser.remove(targetUserId);
        if (likesByTargetUser == null) {
            return List.of();
        }

        Map<List<FilmLikes>, Integer> intersectionOccasion = allLikesGroupedByUser.entrySet().stream()
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