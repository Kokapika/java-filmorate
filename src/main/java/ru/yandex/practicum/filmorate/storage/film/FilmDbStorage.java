package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;
import static ru.yandex.practicum.filmorate.storage.genre.FilmGenreDbStorage.genreBuilder;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film createFilm(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rate", film.getRate());
        values.put("mpa_rating_id", film.getMpa().getId());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE films " +
                "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ? " +
                "WHERE film_id = ?";
        int result = jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (result != 1) {
            throw new NotFoundException("Фильм не найден");
        }
        return getFilmById(film.getId());
    }

    @Override
    public void deleteAllFilms() {
        String sql = "DELETE FROM films";
        jdbcTemplate.update(sql);
    }

    @Override
    public int deleteFilmById(int id) {
        final String sql = "DELETE FROM films WHERE film_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public Film getFilmById(int id) {
        String sql = "SELECT * " +
                "FROM films AS f " +
                "LEFT JOIN mpa_ratings AS m ON m.mpa_rating_id = f.mpa_rating_id " +
                "WHERE f.film_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::filmMapper, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("Фильм не найден");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "SELECT * " +
                "FROM films AS f " +
                "LEFT JOIN mpa_ratings AS m ON m.mpa_rating_id = f.mpa_rating_id";
        List<Film> films = jdbcTemplate.query(sql, this::filmMapper);
        addGenresToFilms(films);
        return films;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        final String sql = "SELECT * " +
                "FROM films AS f " +
                "LEFT JOIN mpa_ratings AS m ON m.mpa_rating_id = f.mpa_rating_id " +
                "LEFT OUTER JOIN film_likes AS fl on f.film_id = fl.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(fl.film_id) " +
                "DESC LIMIT ?";
        List<Film> films = jdbcTemplate.query(sql, this::filmMapper, count);
        addGenresToFilms(films);
        return films;
    }

    private Film filmMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getDate("release_date").toLocalDate(),
                rs.getString("description"),
                rs.getInt("duration"),
                rs.getInt("rate"),
                new Mpa(rs.getInt("mpa_rating_id"), rs.getString("mpa_name")));
    }

    private void addGenresToFilms(List<Film> films) {
        String filmIds = films.stream()
                .map(Film::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        final String sql = "SELECT * FROM genres AS g, film_genres AS fg WHERE fg.genre_id" +
                " = g.genre_id AND fg.film_id IN (" + filmIds + ")";
        jdbcTemplate.query(sql, (rs) -> {
            final Film film = filmById.get(rs.getInt("film_id"));
            film.addGenreToFilm(genreBuilder(rs));
        });
    }
}