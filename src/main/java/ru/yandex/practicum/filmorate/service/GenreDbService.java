package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreDbService {
    private final GenreDao genreDao;

    public Genre getGenreById(Integer genreId) {
        if (!genreDao.containsGenre(genreId)) {
            throw new InvalidIdException("Жанр с id = " + genreId + " не существует");
        }
        Genre result = genreDao.getGenreById(genreId);
        log.debug("Получен жанр с id = " + genreId);
        return result;
    }

    public List<Genre> getGenres() {
        List<Genre> result = genreDao.getGenres();
        log.debug("Получены все жанры");
        return result;
    }
}
