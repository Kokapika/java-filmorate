package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.FilmGenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreDbService {
    private final FilmGenreStorage filmGenreStorage;

    public List<Genre> getGenres() {
        return filmGenreStorage.getAllGenres();
    }

    public Genre getGenreById(int id) {
        if (id < 1 || id > 6) {
            throw new NotFoundException("Некорректно введен ID жанра");
        }
        return filmGenreStorage.getGenreById(id);
    }
}
