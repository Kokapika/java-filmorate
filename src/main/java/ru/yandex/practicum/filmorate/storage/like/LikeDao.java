package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EmptyResultException;

import java.util.Objects;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeDao implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Integer filmId, Integer userId) {
        jdbcTemplate.update(""
                + "INSERT INTO film_likes (film_id, user_id) "
                + "VALUES (?, ?)", filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        jdbcTemplate.update(""
                + "DELETE FROM film_likes "
                + "WHERE film_id=? "
                + "AND user_id=?", filmId, userId);
    }

    @Override
    public Integer countLikes(Integer filmId) {
        return Objects.requireNonNull(
                jdbcTemplate.queryForObject(format(""
                        + "SELECT COUNT(*) "
                        + "FROM film_likes "
                        + "WHERE film_id=%d", filmId), Integer.class));
    }

    @Override
    public boolean containsLike(Integer filmId, Integer userId) {
        try {
            jdbcTemplate.queryForObject(format(""
                    + "SELECT film_id, user_id "
                    + "FROM film_likes "
                    + "WHERE film_id=%d "
                    + "AND user_id=%d", filmId, userId), new LikeMapper());
            return true;
        } catch (EmptyResultException e) {
            return false;
        }
    }
}
