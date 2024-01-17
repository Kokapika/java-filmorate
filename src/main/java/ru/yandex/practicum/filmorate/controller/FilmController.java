package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.enums.SortBy;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
@Validated
@Slf4j
public class FilmController {
    private final FilmService filmDbService;
    private final LikeService likesDbService;

    @GetMapping
    public List<Film> getFilms() {
        return filmDbService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer id) {
        return filmDbService.getFilmById(id);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        checkFilm(film);
        log.info("addFilm film {} ", film.getName());
        return filmDbService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        checkFilm(film);
        log.info("updateFilm film {} ", film.getId());
        return filmDbService.updateFilm(film);
    }

    @DeleteMapping("/{filmId}")
    public int deleteFilmById(@PathVariable("filmId") int filmId) {
        return filmDbService.deleteFilmById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film addLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        return likesDbService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        likesDbService.deleteLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@Positive @RequestParam(name = "count", defaultValue = "10", required = false) Integer count,
                                      @RequestParam(required = false) Integer genreId,
                                      @RequestParam(required = false) Integer year) {
        return filmDbService.getPopularFilms(count, genreId, year);
    }

    private void checkFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895 года. Введено: "
                    + film.getReleaseDate());
    }

    /**
     * Получает список фильмов режиссера по указанному идентификатору,
     * отсортированный по количеству лайков или году выпуска в соответствии
     * с заданным параметром сортировки.
     *
     * @param directorId Идентификатор режиссера.
     * @param sortBy     Параметр сортировки, может принимать значения "year" или "likes".
     * @return Список фильмов режиссера отсортированный по указанному параметру.
     */
    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirector(
            @PathVariable Integer directorId,
            @RequestParam String sortBy) {
        log.info("getFilmsByDirector director {} sortBy {} ", directorId, sortBy);
        SortBy sortByEnum = SortBy.valueOf(sortBy.toUpperCase());
        return filmDbService.getFilmsByDirector(directorId, sortByEnum);
    }

    /**
     * Получает список фильмов, которые отметили 2 пользователя.
     *
     * @param userId   Идентификатор пользователя.
     * @param friendId Идентификатор 2го пользователя .
     * @return Список фильмов по указанным параметрам.
     */
    @GetMapping("/common")
    public List<Film> getCommonFilms(
            @RequestParam Integer userId,
            @RequestParam Integer friendId) {
        log.info("getCommonFilms for userId {} friendId {}", userId, friendId);
        return filmDbService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/search")
    public List<Film> getSearchFilms(@RequestParam String query,
                                     @RequestParam String by) {
        return filmDbService.getSearchFilms(query, by);
    }
}