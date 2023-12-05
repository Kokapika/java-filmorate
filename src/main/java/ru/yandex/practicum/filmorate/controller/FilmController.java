package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получен POST запрос на добавление фильма");
        validationFilm(film);
        return filmService.getFilmStorage().addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен PUT запрос на обновление фильма");
        validationFilm(film);
        return filmService.getFilmStorage().updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получен PUT запрос на добавление лайка");
        filmService.addLikeToFilm(id, userId);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен GET запрос на получение фильмов");
        return filmService.getFilmStorage().getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        log.info("Получен GET запрос на получение фильма");
        return filmService.getFilmStorage().getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Получен GET запрос на получение популярных фильмов");
        return filmService.getPopularFilms(count);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeToFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Получен DELETE запрос на удаление лайка");
        filmService.deleteLikeToFilm(id, userId);
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
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Валидация при добавлении/обновлении фильма не пройдена");
            throw new ValidationException("Дата релиза фильма не должна быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.error("Валидация при добавлении/обновлении фильма не пройдена");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
