package ru.yandex.practicum.filmorate.controller;

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
class LikeDbTest {

    private final LikeDbStorage likeDbStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Test
    void saveLike() {
        Film testFilm = new Film("Аватар",
                "Расплескалась синева, расплескааааалась",
                LocalDate.of(2022, 1, 1),
                200,
                new Mpa(1, "G"),
                4);

        filmStorage.createFilm(testFilm);

        Film testFilm2 = new Film("Аватар2",
                "Расплескалась синева, расплескаааааалась",
                LocalDate.of(2023, 1, 1),
                200,
                new Mpa(1, "G"),
                4);

        filmStorage.createFilm(testFilm2);

        User testUser = new User("abububugaga@mail.ru", "Koka", "Kokapika",
                LocalDate.of(1999, 1, 1));

        userStorage.createUser(testUser);

        User testUser2 = new User("abuga@mail.ru", "Kapika", "Kokapika",
                LocalDate.of(2000, 1, 1));

        userStorage.createUser(testUser2);

        likeDbStorage.addLike(testFilm.getId(), testUser.getId());
        likeDbStorage.addLike(testFilm2.getId(), testUser2.getId());

        List<Film> likesFilm = filmStorage.getPopularFilms(10);

        assertEquals(likesFilm.size(), 2);
    }

    @Test
    void removeLike() {
        Film testFilm = new Film("Аватар",
                "Расплескалась синева, расплескааааалась",
                LocalDate.of(2022, 1, 1),
                200,
                new Mpa(1, "G"),
                4);

        filmStorage.createFilm(testFilm);

        Film testFilm2 = new Film("Аватар2",
                "Расплескалась синева, расплескаааааалась",
                LocalDate.of(2023, 1, 1),
                200,
                new Mpa(1, "G"),
                4);

        filmStorage.createFilm(testFilm2);

        User testUser = new User("abububugaga@mail.ru", "Koka", "Kokapika",
                LocalDate.of(1999, 1, 1));

        userStorage.createUser(testUser);

        User testUser2 = new User("abuga@mail.ru", "Kapika", "Kokapika",
                LocalDate.of(2000, 1, 1));

        userStorage.createUser(testUser2);

        likeDbStorage.addLike(testFilm.getId(), testUser.getId());
        likeDbStorage.addLike(testFilm2.getId(), testUser2.getId());

        assertEquals(2, likeDbStorage.getAllLikes().size());
        likeDbStorage.deleteLike(testFilm2.getId(), testUser2.getId());

        assertEquals(1, likeDbStorage.getAllLikes().size());
    }
}
