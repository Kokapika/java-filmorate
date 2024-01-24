package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.enums.SearchBy;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.model.enums.SortBy;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final MpaStorage mpaStorage;
    private final DirectorStorage directorStorage;
    private final UserService userService;

    public Film addFilm(Film film) {
        Film newFilm = filmStorage.createFilm(film);
        if (!film.getGenres().isEmpty()) {
            List<Genre> uniqueGenres = film.getGenres().stream()
                    .distinct()
                    .collect(Collectors.toList());
            filmGenreStorage.updateGenres(film.getId(), uniqueGenres);
        }
        if (!film.getDirectors().isEmpty()) {
            List<Director> uniqueDirectors = film.getDirectors().stream()
                    .distinct()
                    .collect(Collectors.toList());
            directorStorage.updateFilmDirectors(film.getId(), uniqueDirectors);
        }
        newFilm.setGenres(filmGenreStorage.getGenresByFilmId(newFilm.getId()));
        newFilm.setMpa(mpaStorage.getMpaById(newFilm.getMpa().getId()));
        newFilm.setDirectors(directorStorage.getFilmDirectorsById(film.getId()));
        film.setId(film.getId());
        return newFilm;
    }

    public Film updateFilm(Film film) {
        Film updateFilm = filmStorage.updateFilm(film);
        filmGenreStorage.deleteGenresByFilmId(film.getId());
        if (film.getGenres() != null) {
            List<Genre> uniqueGenres = film.getGenres().stream()
                    .distinct()
                    .collect(Collectors.toList());
            filmGenreStorage.updateGenres(film.getId(), uniqueGenres);
        }
        List<Director> uniqueDirectors = film.getDirectors()
                .stream()
                .distinct()
                .collect(Collectors.toList());
        List<Integer> uniqueDirectorsId = uniqueDirectors.stream()
                .map(director -> director.getId())
                .collect(Collectors.toList());
        List<Integer> oldDirectorsId = directorStorage.getFilmDirectorsById(film.getId())
                .stream()
                .map(director -> director.getId())
                .collect(Collectors.toList());
        if (!CollectionUtils.isEqualCollection(uniqueDirectorsId, oldDirectorsId)) {
            directorStorage.deleteAllDirectorByFilmId(film.getId());
            if (!uniqueDirectors.isEmpty()) {
                directorStorage.updateFilmDirectors(film.getId(), uniqueDirectors);
            }
        }
        updateFilm.setGenres(filmGenreStorage.getGenresByFilmId(film.getId()));
        updateFilm.setMpa(mpaStorage.getMpaById(updateFilm.getMpa().getId()));
        updateFilm.setDirectors(directorStorage.getFilmDirectorsById(film.getId()));
        return updateFilm;
    }

    public List<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    public List<Film> getFilms(List<Integer> filmIds) {
        return filmStorage.getFilms(filmIds);
    }

    public Film getFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);
        film.setGenres(filmGenreStorage.getGenresByFilmId(film.getId()));
        film.setDirectors(directorStorage.getFilmDirectorsById(id));
        return film;
    }

    public int deleteFilmById(int id) {
        return filmStorage.deleteFilmById(id);
    }

    public List<Film> getPopularFilms(Integer count, Integer genreId, Integer year) {
        return filmStorage.getPopularFilms(count, genreId, year);
    }

    public List<Film> getFilmsByDirector(Integer directorId, SortBy sortBy) {
        if (!directorStorage.isDirectorExist(directorId)) {
            throw new NotFoundException("Режиссер не найден id = " + directorId);
        }
        return filmStorage.getFilmsByDirector(directorId, sortBy);
    }

    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        if (!userService.isExistingUser(userId)) {
            throw new NotFoundException("User не найден id = " + userId);
        }
        if (!userService.isExistingUser(friendId)) {
            throw new NotFoundException("Friend не найден id = " + friendId);
        }
        return filmStorage.getCommonFilms(userId, friendId);
    }

    public List<Film> searchFilms(String query, SearchBy searchByEnum) {
        return filmStorage.searchFilms(query, searchByEnum);
    }
}
