package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likesStorage;
    private final UserEventService userEventService;

    public Film addLike(int filmId, int userId) {
        log.info("Проверяем фильм id {} ", filmId);
        Film film = filmStorage.getFilmById(filmId);
        log.info("Проверяем юзера id {} ", userId);
        User user = userStorage.getUserById(userId);
        log.info("Добавляем в ивент {}", filmId);
        userEventService.addEvent(EventType.LIKE, userId, filmId, OperationType.ADD);
        log.info("Добавляем лайк id {} ", filmId);
        likesStorage.addLike(filmId, userId);
        return film;
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        likesStorage.deleteLike(filmId, userId);
        userEventService.addEvent(EventType.LIKE, userId, filmId, OperationType.REMOVE);
        return film;
    }

    public List<FilmLikes> getAllLikes() {
        return likesStorage.getAllLikes();
    }
}
