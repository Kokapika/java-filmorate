package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmDbService {
    private final FilmStorage filmStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final LikeStorage likesStorage;

    @Autowired
    public FilmDbService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                         FilmGenreStorage filmGenreStorage,
                         LikeStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.filmGenreStorage = filmGenreStorage;
        this.likesStorage = likesStorage;
    }

    public Film addFilm(Film film) {
        Integer id = filmStorage.addFilm(film);
        filmGenreStorage.addGenres(id, film.getGenres());
        return getFilmById(id);
    }

    public Film updateFilm(Film film) {
        filmStorage.updateFilm(film);
        filmGenreStorage.updateGenres(film.getId(), film.getGenres());
        return getFilmById(film.getId());
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms().stream()
                .map(film -> film.toBuilder()
                        .likes(likesStorage.getLikes(film.getId()))
                        .genres(filmGenreStorage.getGenres(film.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getFilmById(id).toBuilder()
                .likes(likesStorage.getLikes(id))
                .genres(filmGenreStorage.getGenres(id))
                .build();
    }
}
