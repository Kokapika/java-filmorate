package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(Integer id);

    boolean containsFilm(Integer id);

    void addGenres(Integer id, Set<Genre> genres);

    void updateGenres(Integer id, Set<Genre> genres);

    Set<Genre> getGenres(Integer id);
}
