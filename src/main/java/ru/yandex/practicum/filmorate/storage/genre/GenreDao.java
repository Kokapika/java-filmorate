package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EmptyResultException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDao implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(Integer genreId) {
        return jdbcTemplate.queryForObject(format(""
                + "SELECT genre_id, genre_name "
                + "FROM genres "
                + "WHERE genre_id=%d", genreId), new GenreMapper());
    }

    @Override
    public List<Genre> getGenres() {
        return jdbcTemplate.query(""
                + "SELECT genre_id, genre_name "
                + "FROM genres "
                + "ORDER BY genre_id", new GenreMapper());
    }

    @Override
    public boolean containsGenre(Integer genreId) {
        try {
            getGenreById(genreId);
            return true;
        } catch (EmptyResultException e) {
            return false;
        }
    }
}
