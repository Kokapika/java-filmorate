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
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDbTest {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final UserStorage userStorage;

    @Test
    void createFilm() {
        Film testFilm = new Film("Аватар",
                "Расплескалась синева, расплескааааалась",
                LocalDate.of(2022, 1, 1),
                200,
                new Mpa(1, "G"),
                4);

        filmStorage.createFilm(testFilm);

        assertEquals("G", testFilm.getMpa().getName());
        assertEquals(1, testFilm.getId());
        assertEquals(1, filmStorage.getAllFilms().size());

        System.out.println(testFilm);
    }

    @Test
    void updateFilm() {
        Film testFilm = new Film("Аватар",
                "Расплескалась синева, расплескааааалась",
                LocalDate.of(2022, 1, 1),
                200,
                new Mpa(1, "G"),
                4);

        filmStorage.createFilm(testFilm);

        Film updatedFilm = new Film(1, "Аватар",
                LocalDate.of(2022, 1, 1),
                "Расплескалась синева, расплескааааалась", 200,
                4, new Mpa(2, "PG"));

        filmStorage.updateFilm(updatedFilm);

        assertEquals("PG", updatedFilm.getMpa().getName());
        assertEquals(1, updatedFilm.getId());
        assertEquals(1, filmStorage.getAllFilms().size());

        System.out.println(updatedFilm);
    }

    @Test
    void findAllFilms() {
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
        assertEquals(2, filmStorage.getAllFilms().size());
    }

    @Test
    void getFilmById() {
        Film testFilm = new Film("Аватар",
                "Расплескалась синева, расплескааааалась",
                LocalDate.of(2022, 1, 1),
                200,
                new Mpa(1, "G"),
                4);

        filmStorage.createFilm(testFilm);

        Film testGet = filmStorage.getFilmById(1);
        assertEquals("Аватар", testGet.getName());
    }

    @Test
    void getPopularFilms() {
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

        likeStorage.addLike(testFilm.getId(), testUser.getId());
        likeStorage.addLike(testFilm2.getId(), testUser.getId());

        List<Film> popularFilm = filmStorage.getPopularFilms(2);
        assertEquals(popularFilm.size(), 2);
    }

    @Test
    void deleteFilmById() {
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

        assertEquals(2, filmStorage.getAllFilms().size());
        filmStorage.deleteFilmById(2);
        assertEquals(1, filmStorage.getAllFilms().size());
        filmStorage.deleteFilmById(1);
        assertEquals(0, filmStorage.getAllFilms().size());
    }
}