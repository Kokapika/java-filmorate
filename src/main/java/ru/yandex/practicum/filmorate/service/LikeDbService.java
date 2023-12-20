package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.already.added.FilmLikeAlreadyAddedException;
import ru.yandex.practicum.filmorate.exception.not.found.FilmLikeNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeDbService {
    private final FilmDbService filmDbService;
    private final UserDbService userDbService;
    private final LikeStorage likesStorage;

    public void addLike(Integer filmId, Integer userId) {
        Film film = filmDbService.getFilmById(filmId);
        User user = userDbService.getUserById(userId);

        if (likesStorage.getLikes(filmId).contains(userId))
            throw new FilmLikeAlreadyAddedException(String.format("Пользователь %s уже поставил лайк фильму %s.",
                    user.getName(), film.getName()));
        likesStorage.addLike(filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        Film film = filmDbService.getFilmById(filmId);
        User user = userDbService.getUserById(userId);

        if (!likesStorage.getLikes(filmId).contains(userId)) {
            throw new FilmLikeNotFoundException(String.format("Пользователь %s не ставил лайк фильму %s, поэтому " +
                    "лайк не может быть удалён.", user.getName(), film.getName()));
        }
        likesStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        return likesStorage.getPopularFilms(count).stream()
                .map(filmDbService::getFilmById)
                .collect(Collectors.toList());
    }
}
