package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteAllFilms();

    List<Film> getAllFilms();

    Film getFilmById(int id);

    List<Film> getPopularFilms(Integer count, Integer genreId, Integer year);

    int deleteFilmById(int id);

    List<Film> getFilmsByDirector(Integer directorId, String sortBy);
}
