package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmLikes;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.user_event.EventType;
import ru.yandex.practicum.filmorate.model.user_event.OperationType;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeDbService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likesStorage;
    private final UserEventDbService userEventDbService;

    public Film addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        likesStorage.addLike(filmId, userId);
        userEventDbService.addEvent(EventType.LIKE, userId, filmId, OperationType.ADD);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        likesStorage.deleteLike(filmId, userId);
        userEventDbService.addEvent(EventType.LIKE, userId, filmId, OperationType.REMOVE);
        return film;
    }

    public List<FilmLikes> getAllLikes() {
        return likesStorage.getAllLikes();
    }
}
