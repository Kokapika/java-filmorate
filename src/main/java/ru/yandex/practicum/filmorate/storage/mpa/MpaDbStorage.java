package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.not.found.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM mpa_ratings";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        Mpa.valueOf(rs.getString("name")))
                .stream().sorted(Enum::compareTo).collect(Collectors.toList());
    }

    @Override
    public Mpa getMpaById(Integer id) {
        String sql = "SELECT * FROM mpa_ratings WHERE mpa_rating_id = ?";
        SqlRowSet mpaRow = jdbcTemplate.queryForRowSet(sql, id);
        if (!mpaRow.next())
            throw new MpaNotFoundException(String.format("Рейтинга MPA с id %d не существует", id));
        return Mpa.valueOf(mpaRow.getString("name"));
    }
}
