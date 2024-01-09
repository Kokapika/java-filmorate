package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.genre.FilmGenreStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbTest {

    private final FilmGenreStorage filmGenreStorage;

    @Test
    void getById() {
        assertEquals("Комедия", filmGenreStorage.getGenreById(1).getName());
        assertEquals("Боевик", filmGenreStorage.getGenreById(6).getName());
    }

    @Test
    void getAllGenres() {
        assertEquals(6, filmGenreStorage.getAllGenres().size());
    }
}