package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EmptyResultException;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.exception.ElementAlreadyExistsException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDao;
import ru.yandex.practicum.filmorate.storage.rating_mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmDbService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeDao likeDao;
    private final MpaDao mpaDao;

    public Film addFilm(Film film) {
        if (filmStorage.containsFilm(film.getId())) {
            throw new ElementAlreadyExistsException("Фильм уже существует");
        }
        Film result = filmStorage.addFilm(film);
        filmStorage.addGenres(result.getId(), film.getGenres());
        result.setGenres(filmStorage.getGenres(result.getId()));
        log.debug("В хранилище добавлен фильм: " + film.getName());
        return result;
    }

    public Film updateFilm(Film film) {
        if (!filmStorage.containsFilm(film.getId())) {
            throw new EmptyResultException("Фильм в хранилище не существует");
        }
        Film result = filmStorage.updateFilm(film);
        filmStorage.updateGenres(result.getId(), film.getGenres());
        result.setGenres(filmStorage.getGenres(result.getId()));
        result.setMpa(mpaDao.getMpaById(result.getMpa().getId()));
        log.debug("Обновлен фильм: " + film.getName());
        return result;
    }

    public Film getFilmById(Integer filmId) {
        if (!filmStorage.containsFilm(filmId)) {
            throw new InvalidIdException("Неверный идентификатор запроса фильма");
        }
        Film film = filmStorage.getFilmById(filmId);
        film.setGenres(filmStorage.getGenres(filmId));
        film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
        log.debug("Получен фильм с id = " + filmId);
        return film;
    }

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        for (Film film : films) {
            film.setGenres(filmStorage.getGenres(film.getId()));
            film.setMpa(mpaDao.getMpaById(film.getMpa().getId()));
        }
        log.debug("Получены все фильмы");
        return films;
    }

    public List<Film> getPopularFilms(Integer count) {
        List<Film> popularFilms = filmStorage.getFilms().stream()
                .sorted(this::likeCompare)
                .limit(count)
                .collect(Collectors.toList());
        log.debug("Получены популярные фильмы");
        return popularFilms;
    }

    public void addLike(Integer filmId, Integer userId) {
        if (!filmStorage.containsFilm(filmId)) {
            throw new EmptyResultException("Фильм не существует");
        }
        if (!userStorage.containsUser(userId)) {
            throw new EmptyResultException("Пользователь не существует");
        }
        if (likeDao.containsLike(filmId, userId)) {
            throw new ElementAlreadyExistsException("Пользователь уже ранее ставил лайк фильму");
        }
        likeDao.addLike(filmId, userId);
        log.debug("Добавлен лайк");
    }

    public void deleteLike(Integer filmId, Integer userId) {
        if (!filmStorage.containsFilm(filmId)) {
            throw new EmptyResultException("Фильм не существует");
        }
        if (!userStorage.containsUser(userId)) {
            throw new EmptyResultException("Пользователь не существует");
        }
        if (!likeDao.containsLike(filmId, userId)) {
            throw new EmptyResultException("Пользователь ранее не ставил лайк фильму");
        }
        likeDao.deleteLike(filmId, userId);
        log.debug("Удален лайк");
    }

    private Integer likeCompare(Film film, Film otherFilm) {
        return Integer.compare(likeDao.countLikes(otherFilm.getId()), likeDao.countLikes(film.getId()));
    }
}
