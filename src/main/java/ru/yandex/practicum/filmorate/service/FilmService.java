//package ru.yandex.practicum.filmorate.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
//import ru.yandex.practicum.filmorate.storage.user.UserStorage;
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@Slf4j
//@Service
//public class FilmService {
//    private final FilmStorage filmStorage;
//    private final UserStorage userStorage;
//
//    public Film addFilm(Film film) {
//        return filmStorage.addFilm(film);
//    }
//
//    public Film updateFilm(Film film) {
//        return filmStorage.updateFilm(film);
//    }
//
//    public List<Film> getFilms() {
//        return filmStorage.getFilms();
//    }
//
//    public Film getFilmById(Integer id) {
//        return filmStorage.getFilmById(id);
//    }
//
//    public void addLikeToFilm(Integer filmId, Integer userId) {
//        Film film = filmStorage.getFilmById(filmId);
//        userStorage.getUserById(userId);
//        film.getLikes().add(userId);
//        log.debug("Пользователь с id=" + userId + " поставил лайк фильму с id=" + filmId);
//    }
//
//    public void deleteLikeToFilm(Integer filmId, Integer userId) {
//        Film film = filmStorage.getFilmById(filmId);
//        userStorage.getUserById(userId);
//        film.getLikes().remove(userId);
//        log.debug("Пользователь с id=" + userId + " удалил лайк фильма с id=" + filmId);
//    }
//
//    public List<Film> getPopularFilms(Integer count) {
//        List<Film> films = filmStorage.getFilms();
//        return films.stream()
//                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
//                .limit(count)
//                .collect(Collectors.toList());
//    }
//}
