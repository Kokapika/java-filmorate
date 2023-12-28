package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RecommendationServiceTest {

    private final RecommendationService recommendationService;
    private final LikeDbStorage likeDbStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Test
    void getRecommendationsTest() {
        Film testFilm1 = new Film("testFilm1", "testFilm1", LocalDate.of(2022, 1, 1), 200, new Mpa(1, "G"), 1);
        filmStorage.createFilm(testFilm1);

        Film testFilm2 = new Film("testFilm2", "testFilm2", LocalDate.of(2023, 1, 1), 200, new Mpa(1, "G"), 1);
        filmStorage.createFilm(testFilm2);

        Film testFilm3 = new Film("testFilm3", "testFilm3", LocalDate.of(2023, 1, 1), 200, new Mpa(1, "G"), 1);
        filmStorage.createFilm(testFilm3);

        User testUser1 = new User("testUser1@mail.ru", "testUser1", "testUser1", LocalDate.of(1999, 1, 1));
        userStorage.createUser(testUser1);

        User testUser2 = new User("testUser2@mail.ru", "testUser2", "testUser2", LocalDate.of(2000, 1, 1));
        userStorage.createUser(testUser2);

        User testUser3 = new User("testUser3@mail.ru", "testUser3", "testUser3", LocalDate.of(2000, 1, 1));
        userStorage.createUser(testUser3);

        likeDbStorage.addLike(testFilm1.getId(), testUser1.getId());
        likeDbStorage.addLike(testFilm2.getId(), testUser1.getId());

        likeDbStorage.addLike(testFilm1.getId(), testUser2.getId());
        likeDbStorage.addLike(testFilm2.getId(), testUser2.getId());
        likeDbStorage.addLike(testFilm3.getId(), testUser2.getId());

        likeDbStorage.addLike(testFilm2.getId(), testUser3.getId());
        likeDbStorage.addLike(testFilm3.getId(), testUser3.getId());

        List<Film> films = recommendationService.getRecommendations(1);
        assertEquals(films.size(), 1);
        assertEquals(films.get(0), testFilm3);
    }
}