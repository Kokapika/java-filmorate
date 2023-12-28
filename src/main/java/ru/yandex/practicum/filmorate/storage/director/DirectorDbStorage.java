package ru.yandex.practicum.filmorate.storage.director;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Director createDirector(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("director_id");
        director.setId(simpleJdbcInsert.executeAndReturnKey(Map.of("director_name", director.getName())).intValue());
        return director;
    }

    @Override
    public Director getDirectorById(int id) {
        String sql = "SELECT * " +
                "FROM DIRECTORS " +
                "WHERE DIRECTOR_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, this::directorMapper, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("Директор не найден");
        }
    }

    @Override
    public List<Director> getAllDirectors() {
        String sql = "SELECT * FROM DIRECTORS";
        return jdbcTemplate.query(sql, this::directorMapper);
    }

    @Override
    public Director updateDirector(Director director) {
        String sql = "UPDATE directors SET DIRECTOR_NAME = ? WHERE DIRECTOR_ID = ?";
        int result = jdbcTemplate.update(sql, director.getName(), director.getId());
        if (result != 1) {
            throw new NotFoundException("Директор не найден");
        }
        return director;
    }

    @Override
    public int deleteDirectorById(int id) {
        String sql = "DELETE FROM DIRECTORS WHERE DIRECTOR_ID = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateFilmDirectors(Integer filmId, List<Director> directors) {
        jdbcTemplate.batchUpdate("INSERT INTO FILM_DIRECTOR (FILM_ID, DIRECTOR_ID) values (?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, directors.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return directors.size();
                    }
                });
    }

    @Override
    public List<Director> getFilmDirectorsById(Integer id) {
        String sql = "SELECT d.DIRECTOR_ID, d.DIRECTOR_NAME " +
                "FROM DIRECTORS AS d " +
                "JOIN FILM_DIRECTOR AS fd ON d.DIRECTOR_ID = fd.DIRECTOR_ID " +
                "WHERE fd.film_id = ?";
        return jdbcTemplate.query(sql, this::directorMapper, id);
    }

    @Override
    public void deleteAllDirectorByFilmId(Integer id) {
        String sqlDelete = "DELETE FROM FILM_DIRECTOR WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlDelete, id);
    }

    private Director directorMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Director(rs.getInt("director_id"),
                rs.getString("director_name"));
    }
    @Override
    public boolean doesDirectorExist(Integer directorId) {
        String sql = "SELECT 1 FROM DIRECTORS WHERE DIRECTOR_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> true, directorId).stream().findFirst().orElse(false);
    }
}
