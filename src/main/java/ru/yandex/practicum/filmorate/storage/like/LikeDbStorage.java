package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmLikes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public int addLike(int filmId, int userId) {
        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        updateRate(filmId);
        return jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public int deleteLike(int filmId, int userId) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        updateRate(filmId);
        return jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<FilmLikes> getAllLikes() {
        String sql = "SELECT * FROM film_likes";
        return jdbcTemplate.query(sql, this::likeMapper);
    }

    private void updateRate(int filmId) {
        jdbcTemplate.update("UPDATE films AS f " +
                "SET rate = (" +
                "SELECT COUNT(fl.user_id) " +
                "FROM film_likes AS fl " +
                "WHERE fl.film_id = f.film_id" +
                ") " +
                "WHERE film_id = ?", filmId);
    }

    private FilmLikes likeMapper(ResultSet rs, int rowNum) throws SQLException {
        int filmId = rs.getInt("film_id");
        int userId = rs.getInt("user_id");
        return new FilmLikes(filmId, userId);
    }
}
