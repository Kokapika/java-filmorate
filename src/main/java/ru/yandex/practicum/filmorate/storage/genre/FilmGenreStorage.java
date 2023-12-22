package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreStorage {
    Genre getGenreById(int id);

    List<Genre> getAllGenres();

    List<Genre> getGenresByFilmId(int filmId);

    void updateGenres(int filmId, List<Genre> genres);

    void deleteGenresByFilmId(int filmId);
}
