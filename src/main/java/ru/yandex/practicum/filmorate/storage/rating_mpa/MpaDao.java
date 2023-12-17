package ru.yandex.practicum.filmorate.storage.rating_mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EmptyResultException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class MpaDao implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpaById(Integer mpaId) {
        return jdbcTemplate.queryForObject(format(""
                + "SELECT mpa_rating_id, rating_name "
                + "FROM mpa_ratings "
                + "WHERE mpa_rating_id=%d", mpaId), new MpaMapper());
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query(""
                + "SELECT mpa_rating_id, rating_name "
                + "FROM mpa_ratings "
                + "ORDER BY mpa_rating_id", new MpaMapper());
    }

    @Override
    public boolean containsMpa(Integer mpaId) {
        try {
            getMpaById(mpaId);
            return true;
        } catch (EmptyResultException e) {
            return false;
        }
    }
}
