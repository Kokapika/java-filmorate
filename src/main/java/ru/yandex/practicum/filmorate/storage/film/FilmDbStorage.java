package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
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

@Slf4j
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
    public List<Film> getFilmsByDirector(Integer directorId, String sortBy) {
        String sql = "SELECT * " +
                "FROM FILMS as F " +
                "LEFT JOIN mpa_ratings AS m ON m.mpa_rating_id = f.mpa_rating_id " +
                "RIGHT JOIN FILM_DIRECTOR FD on F.FILM_ID = FD.FILM_ID " +
                "WHERE DIRECTOR_ID = ? " +
                "ORDER BY RELEASE_DATE";
        if (sortBy.equals("likes")) {
            sql = "SELECT * " +
                    "FROM FILMS as F " +
                    "LEFT JOIN mpa_ratings AS m ON m.mpa_rating_id = f.mpa_rating_id " +
                    "RIGHT JOIN FILM_DIRECTOR FD on F.FILM_ID = FD.FILM_ID " +
                    "LEFT JOIN FILM_LIKES FL on f.FILM_ID = FL.FILM_ID " +
                    "WHERE DIRECTOR_ID = ? " +
                    "GROUP BY f.film_id " +
                    "ORDER BY COUNT(fl.FILM_ID)";
        }
        List<Film> films = jdbcTemplate.query(sql, this::filmMapper, directorId);
        addGenresToFilms(films);
        addDirectorsToFilms(films);
        return films;
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
        addDirectorsToFilms(films);
        return films;
    }

    @Override
    public List<Film> getPopularFilms(Integer count, Integer genreId, Integer year) {
        if (genreId != null && year != null) {
            String sql = "SELECT f.*, mr.*, COUNT(fl.USER_ID) " +
                    "FROM films AS f " +
                    "LEFT JOIN mpa_ratings AS mr ON mr.mpa_rating_id = f.mpa_rating_id " +
                    "LEFT OUTER JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                    "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                    "WHERE fg.genre_id = ? AND EXTRACT(YEAR FROM CAST(f.release_date AS DATE)) = ? " +
                    "GROUP BY f.film_id " +
                    "ORDER BY f.rate " +
                    "DESC LIMIT ?";
            List<Film> films = jdbcTemplate.query(sql, this::filmMapper, genreId, year, count);
            addGenresToFilms(films);
            return films;
        } else if (genreId != null) {
            String sql = "SELECT f.*, mr.*, COUNT(fl.USER_ID) " +
                    "FROM films AS f " +
                    "LEFT JOIN mpa_ratings AS mr ON mr.mpa_rating_id = f.mpa_rating_id " +
                    "LEFT OUTER JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                    "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                    "WHERE fg.genre_id = ? " +
                    "GROUP BY f.film_id " +
                    "ORDER BY f.rate " +
                    "DESC LIMIT ?";
            List<Film> films = jdbcTemplate.query(sql, this::filmMapper, genreId, count);
            addGenresToFilms(films);
            return films;
        } else if (year != null) {
            String sql = "SELECT f.*, mr.*, COUNT(fl.USER_ID) " +
                    "FROM films AS f " +
                    "LEFT JOIN mpa_ratings AS mr ON mr.mpa_rating_id = f.mpa_rating_id " +
                    "LEFT OUTER JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                    "WHERE EXTRACT(YEAR FROM CAST(f.release_date AS DATE)) = ? " +
                    "GROUP BY f.film_id " +
                    "ORDER BY f.rate " +
                    "DESC LIMIT ?";
            List<Film> films = jdbcTemplate.query(sql, this::filmMapper, year, count);
            addGenresToFilms(films);
            return films;
        } else {
            String sql = "SELECT f.*, mr.*, COUNT(fl.USER_ID) " +
                    "FROM films AS f " +
                    "LEFT JOIN mpa_ratings AS mr ON mr.mpa_rating_id = f.mpa_rating_id " +
                    "LEFT OUTER JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                    "GROUP BY f.film_id " +
                    "ORDER BY f.rate DESC " +
                    "LIMIT ?";
            List<Film> films = jdbcTemplate.query(sql, this::filmMapper, count);
            addGenresToFilms(films);
            return films;
        }
        String sqlFilter;

        if (genreId != null && year != null) {
            sqlFilter = "WHERE fg.genre_id = " + genreId + " AND EXTRACT(YEAR FROM CAST(f.release_date AS DATE)) = " + year;
        } else if (genreId != null) {
            sqlFilter = "WHERE fg.genre_id = " + genreId;
        } else if (year != null) {
            sqlFilter = "WHERE EXTRACT(YEAR FROM CAST(f.release_date AS DATE)) = " + year;
        } else {
            sqlFilter = "";
        }

        String sql = "SELECT f.*, mr.*, COUNT(fl.USER_ID) " +
                "FROM films AS f " +
                "LEFT JOIN mpa_ratings AS mr ON mr.mpa_rating_id = f.mpa_rating_id " +
                "LEFT JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                sqlFilter + " " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(fl.USER_ID) " +
                "DESC LIMIT ?";
        List<Film> films = jdbcTemplate.query(sql, this::filmMapper, count);
        addGenresToFilms(films);
        return films;
    }

    @Override
    public List<Film> getSearchFilms(String query, String by) {
        String sqlFilter = null;

        if (by.equals("title")) {
            sqlFilter = "WHERE LOWER(f.name) LIKE LOWER('%" + query + "%') ";
        }
        if (by.equals("director")) {
            sqlFilter = "WHERE LOWER(d.director_name) LIKE LOWER('%" + query + "%') ";
        }
        if (by.equals("title,director") || by.equals("director,title")) {
            sqlFilter = "WHERE LOWER(f.name) LIKE LOWER('%" + query + "%') " +
                    "OR LOWER(d.director_name) LIKE LOWER('%" + query + "%') ";
        }

        String sql = "SELECT f.*, mr.*, d.* " +
                "FROM films AS f " +
                "LEFT JOIN mpa_ratings AS mr ON mr.mpa_rating_id = f.mpa_rating_id " +
                "LEFT JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                "LEFT JOIN film_director AS fd ON f.film_id = fd.film_id " +
                "LEFT JOIN directors AS d ON d.director_id = fd.director_id " +
                sqlFilter +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(fl.USER_ID) DESC";

        List<Film> films = jdbcTemplate.query(sql, this::filmMapper);
        addGenresToFilms(films);
        addDirectorsToFilms(films);
        return films;
    }

    @Override
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        final String sql = "SELECT DISTINCT f.*, m.MPA_NAME, COUNT(FiL.USER_ID) as likes " +
                "FROM films AS f " +
                "JOIN mpa_ratings AS m ON m.mpa_rating_id = f.mpa_rating_id " +
                "JOIN FILM_LIKES FiL on f.FILM_ID = FiL.FILM_ID " +
                "where f.FILM_ID in (SELECT DISTINCT fi.FILM_ID " +
                "FROM films AS fi " +
                "JOIN FILM_LIKES FL on fi.FILM_ID = FL.FILM_ID " +
                "JOIN FILM_LIKES FL1 on fi.FILM_ID = FL1.FILM_ID " +
                "WHERE FL.USER_ID = ? AND FL1.USER_ID = ?) " +
                "GROUP BY f.FILM_ID " +
                "ORDER BY likes DESC";
        List<Film> films = jdbcTemplate.query(sql, this::filmMapper, userId, friendId);
        addGenresToFilms(films);
        return films;
    }

    private Film filmMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getDate("release_date").toLocalDate(),
                rs.getString("description"),
                rs.getInt("duration"),
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

    private void addDirectorsToFilms(List<Film> films) {
        String filmIds = films.stream()
                .map(Film::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        final Map<Integer, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        final String sql = "SELECT * " +
                "FROM DIRECTORS AS d, FILM_DIRECTOR AS fd " +
                "WHERE fd.DIRECTOR_ID = d.DIRECTOR_ID AND fd.film_id IN (" + filmIds + ")";
        jdbcTemplate.query(sql, (rs) -> {
            final Film film = filmById.get(rs.getInt("film_id"));
            film.addDirectorToFilm(Director.builder()
                    .id(rs.getInt("director_id"))
                    .name(rs.getString("director_name"))
                    .build());
        });
    }
}