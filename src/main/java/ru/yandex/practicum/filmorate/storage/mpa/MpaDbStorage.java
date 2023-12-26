package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpaById(int id) {
        String sql = "SELECT * FROM mpa_ratings WHERE mpa_rating_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mpaMapper, id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM mpa_ratings";
        return jdbcTemplate.query(sql, this::mpaMapper);
    }

    private Mpa mpaMapper(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("mpa_rating_id");
        String mpaName = rs.getString("mpa_name");
        return new Mpa(id, mpaName);
    }
}
