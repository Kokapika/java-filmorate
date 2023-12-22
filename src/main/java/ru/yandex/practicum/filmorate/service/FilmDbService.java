package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor

public class FilmDbService {
    private final FilmStorage filmStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final MpaStorage mpaStorage;

    public Film addFilm(Film film) {
        Film newFilm = filmStorage.createFilm(film);
        if (film.getGenres() != null) {
            filmGenreStorage.updateGenres(newFilm.getId(), film.getGenres());
        }
        newFilm.setGenres(filmGenreStorage.getGenresByFilmId(newFilm.getId()));
        newFilm.setMpa(mpaStorage.getMpaById(newFilm.getMpa().getId()));
        return newFilm;
    }

    public Film updateFilm(Film film) {
        Film updateFilm = filmStorage.updateFilm(film);
        filmGenreStorage.deleteGenresByFilmId(film.getId());
        if (film.getGenres() != null) {
            filmGenreStorage.updateGenres(film.getId(), film.getGenres());
        }
        updateFilm.setGenres(filmGenreStorage.getGenresByFilmId(film.getId()));
        updateFilm.setMpa(mpaStorage.getMpaById(updateFilm.getMpa().getId()));
        return updateFilm;
    }

    public List<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    public void deleteFilms() {
        filmStorage.deleteAllFilms();
    }

    public Film getFilmById(Integer id) {
        Film film = filmStorage.getFilmById(id);
        film.setGenres(filmGenreStorage.getGenresByFilmId(film.getId()));
        return film;
    }

    public int deleteFilmById(int id) {
        return filmStorage.deleteFilmById(id);
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }
}
