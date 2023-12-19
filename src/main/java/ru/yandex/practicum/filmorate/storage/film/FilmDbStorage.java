package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.not.found.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Integer addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        return simpleJdbcInsert.executeAndReturnKey(new HashMap<>() {{
                put("film_id", film.getId());
                put("name", film.getName());
                put("description", film.getDescription());
                put("release_date", film.getReleaseDate());
                put("duration", film.getDuration());
                put("mpa_rating_id", film.getMpa().getId());
            }
        }).intValue();
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM films AS f LEFT JOIN mpa_ratings AS mr ON f.mpa_rating_id = mr.mpa_rating_id";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                Film.builder()
                        .id(rs.getInt("films.film_id"))
                        .name(rs.getString("films.name"))
                        .description(rs.getString("films.description"))
                        .releaseDate(rs.getDate("films.release_date").toLocalDate())
                        .duration(rs.getInt("films.duration"))
                        .mpa(Mpa.valueOf(rs.getString("mpa_ratings.name")))
                        .build());
    }

    @Override
    public Film getFilmById(Integer id) {
        String sql = "SELECT * FROM films LEFT JOIN mpa_ratings ON films.mpa_rating_id = mpa_ratings.mpa_rating_id " +
                "WHERE film_id = ?";
        try {
            return jdbcTemplate.query(sql, (rs, numRow) ->
                    Film.builder()
                            .id(rs.getInt("film_id"))
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .releaseDate(rs.getDate("release_date").toLocalDate())
                            .duration(rs.getInt("duration"))
                            .mpa(Mpa.valueOf(rs.getString("mpa_ratings.name")))
                            .build(), id).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new FilmNotFoundException(String.format("Фильма с id %d не существует.", id));
        }
    }
}