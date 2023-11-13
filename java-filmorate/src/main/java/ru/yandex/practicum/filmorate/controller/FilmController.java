package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int generateId = 1;
    private final LocalDate DATE = LocalDate.of(1895, 12, 28);
    private final static Logger log = LoggerFactory.getLogger(Film.class);

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Получен POST запрос на добавление фильма");
        validationFilm(film);
        film.setId(generateId);
        films.put(generateId++, film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Получен PUT запрос на обновление фильма");
        validationFilm(film);
        Film f = films.get(film.getId());
        f.setName(film.getName());
        f.setDescription(film.getDescription());
        f.setDuration(film.getDuration());
        f.setReleaseDate(film.getReleaseDate());
        return f;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен GET запрос на получение фильмов");
        return new ArrayList<>(films.values());
    }

    private void validationFilm(Film film) {
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            log.error("Валидация при добавлении/обновлении фильма не пройдена");
            throw new ValidationException("Название фильма не должно быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Валидация при добавлении/обновлении фильма не пройдена");
            throw new ValidationException("Длина описания фильма не должна превышать 200 символов");
        }
        if (film.getDescription().isEmpty() || film.getDescription().isBlank()) {
            log.error("Валидация при добавлении/обновлении фильма не пройдена");
            throw new ValidationException("Описание фильма не должно быть пустым");
        }
        if (film.getReleaseDate().isBefore(DATE)) {
            log.error("Валидация при добавлении/обновлении фильма не пройдена");
            throw new ValidationException("Дата релиза фильма не должна быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.error("Валидация при добавлении/обновлении фильма не пройдена");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
