package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.enums.SearchBy;
import ru.yandex.practicum.filmorate.model.enums.SortBy;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteAllFilms();

    List<Film> getFilms(List<Integer> filmIds);

    List<Film> getAllFilms();

    Film getFilmById(int id);

    List<Film> getPopularFilms(Integer count, Integer genreId, Integer year);

    List<Film> searchFilms(String query, SearchBy searchByEnum);

    int deleteFilmById(int id);

    List<Film> getFilmsByDirector(Integer directorId, SortBy sortBy);

    List<Film> getCommonFilms(Integer userId, Integer friendId);

    boolean isFilmExist(Integer filmId);
}