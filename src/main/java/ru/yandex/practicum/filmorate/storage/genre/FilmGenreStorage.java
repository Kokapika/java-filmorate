package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;

public interface FilmGenreStorage {
    void addGenres(Integer filmId, LinkedHashSet<Genre> genres);

    void updateGenres(Integer filmId, LinkedHashSet<Genre> genres);

    LinkedHashSet<Genre> getGenres(Integer filmId);
}
