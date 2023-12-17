package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EmptyResultException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreMapper;

import java.sql.Date;
import java.util.*;

import static java.lang.String.format;

@Component
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        jdbcTemplate.update(""
                        + "INSERT INTO films (name, description, release_date, duration, mpa_rating_id) "
                        + "VALUES(?, ?, ?, ?, ?)",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId());
        return jdbcTemplate.queryForObject(format(""
                        + "SELECT film_id, name, description, release_date, duration, mpa_rating_id "
                        + "FROM films "
                        + "WHERE name='%s' "
                        + "AND description='%s' "
                        + "AND release_date='%s' "
                        + "AND duration=%d "
                        + "AND mpa_rating_id=%d",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId()), new FilmMapper());
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update(""
                        + "UPDATE films "
                        + "SET name=?, description=?, release_date=?, duration=?, mpa_rating_id=? "
                        + "WHERE film_id=?",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(Integer id) {
        return jdbcTemplate.queryForObject(format(""
                + "SELECT film_id, name, description, release_date, duration, mpa_rating_id FROM films "
                + "WHERE film_id=%d", id), new FilmMapper());
    }

    @Override
    public List<Film> getFilms() {
        return jdbcTemplate.query(""
                + "SELECT film_id, name, description, release_date, duration, mpa_rating_id "
                + "FROM films", new FilmMapper());
    }

    @Override
    public boolean containsFilm(Integer id) {
        try {
            getFilmById(id);
            return true;
        } catch (EmptyResultException e) {
            return false;
        }
    }

    @Override
    public void addGenres(Integer id, Set<Genre> genres) {
        for (Genre genre : genres) {
            jdbcTemplate.update(""
                    + "INSERT INTO film_genres (film_id, genre_id) "
                    + "VALUES (?, ?)", id, genre.getId());
        }
    }

    @Override
    public void updateGenres(Integer id, Set<Genre> genres) {
        deleteGenres(id);
        addGenres(id, genres);
    }

    @Override
    public Set<Genre> getGenres(Integer id) {
        return new HashSet<>(jdbcTemplate.query(format(""
                + "SELECT f.genre_id, g.name "
                + "FROM film_genres AS f "
                + "LEFT OUTER JOIN genres AS g ON f.genre_id = g.genre_id "
                + "WHERE f.film_id=%d "
                + "ORDER BY g.genre_id", id), new GenreMapper()));
    }

    private void deleteGenres(Integer filmId) {
        jdbcTemplate.update(""
                + "DELETE "
                + "FROM film_genres "
                + "WHERE film_id=?", filmId);
    }
//    private final Map<Integer, Film> films = new HashMap<>();
//    private int generateId = 1;
//
//    @Override
//    public Film addFilm(Film film) {
//        film.setId(generateId);
//        films.put(generateId++, film);
//        log.debug("Добавлен новый фильм " + film.getName());
//        return film;
//    }
//
//    @Override
//    public Film updateFilm(Film film) {
//        if (films.containsKey(film.getId())) {
//            films.put(film.getId(), film);
//            log.debug("Обновлена информация о фильме " + film.getName() + ", с id = " + film.getId());
//        } else if (film.getId() == null) {
//            film.setId(generateId++);
//            films.put(film.getId(), film);
//            log.debug("Добавлен новый фильм " + film.getName());
//        } else {
//            log.debug("Не удалось обновить данные о фильме");
//            throw new InvalidIdException("Неверный идентификатор фильма");
//        }
//        return film;
//    }
//
//    @Override
//    public Film getFilmById(Integer id) {
//        Film film = films.get(id);
//        if (film == null) {
//            log.debug("Фильм с id=" + id + " не найден");
//            throw new InvalidIdException("Неверный идентификатор фильма");
//        }
//        return film;
//    }
//
//    @Override
//    public List<Film> getFilms() {
//        return new ArrayList<>(films.values());
//    }
}
