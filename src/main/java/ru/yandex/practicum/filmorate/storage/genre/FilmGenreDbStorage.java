package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;

@Component
@RequiredArgsConstructor
public class FilmGenreDbStorage implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addGenres(Integer filmId, LinkedHashSet<Genre> genres) {
        String sql = "INSERT INTO film_genres (film_id, genre_id) " +
                "VALUES (?, ?)";
        if (genres != null)
            for (Genre genre : genres)
                jdbcTemplate.update(sql, filmId, genre.getId());
    }

    public void updateGenres(Integer filmId, LinkedHashSet<Genre> genres) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", filmId);
        addGenres(filmId, genres);
    }

    @Override
    public LinkedHashSet<Genre> getGenres(Integer filmId) {
        String sql = "SELECT g.name " +
                "FROM film_genres fg " +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = ?";
        return new LinkedHashSet<>(jdbcTemplate.query(sql, (rs, ronNum) ->
                Genre.valueOf(rs.getString("name")), filmId));
    }
}
