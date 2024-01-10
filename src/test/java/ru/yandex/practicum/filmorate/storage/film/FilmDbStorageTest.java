package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    @Test
    void getCommonFilmsIfOneFilmCommon() {
        Film testFilm1 = filmStorage.createFilm(new Film("testFilm1",
                "testFilm1",
                LocalDate.of(2022, 1, 1),
                200, new Mpa(1, "G"), 1));
        Film testFilm2 = filmStorage.createFilm(new Film("testFilm2",
                "testFilm2", LocalDate.of(2023, 1, 1),
                200, new Mpa(1, "G"), 1));

        User testUser = userStorage.createUser(new User("abububugaga@mail.ru", "Koka", "Kokapika",
                LocalDate.of(1999, 1, 1)));
        User testUser2 = userStorage.createUser(new User("abububaga@mail.ru", "Poka", "PokaPika",
                LocalDate.of(1998, 1, 1)));

        likeStorage.addLike(testFilm1.getId(), testUser.getId());
        likeStorage.addLike(testFilm1.getId(), testUser2.getId());

        assertEquals(List.of(testFilm1), filmStorage.getCommonFilms(testUser.getId(), testUser2.getId()));
    }
    @Test
    void getCommonFilmsIfNotCommonFilm() {
        Film testFilm1 = filmStorage.createFilm(new Film("testFilm1",
                "testFilm1",
                LocalDate.of(2022, 1, 1),
                200, new Mpa(1, "G"), 1));
        Film testFilm2 = filmStorage.createFilm(new Film("testFilm2",
                "testFilm2", LocalDate.of(2023, 1, 1),
                200, new Mpa(1, "G"), 1));

        User testUser = userStorage.createUser(new User("abububugaga@mail.ru", "Koka", "Kokapika",
                LocalDate.of(1999, 1, 1)));
        User testUser2 = userStorage.createUser(new User("abububaga@mail.ru", "Poka", "PokaPika",
                LocalDate.of(1998, 1, 1)));


        assertEquals(List.of(), filmStorage.getCommonFilms(testUser.getId(), testUser2.getId()));
    }
    @Test
    void getCommonFilmsIfFilmNotExist() {
        User testUser = userStorage.createUser(new User("abububugaga@mail.ru", "Koka", "Kokapika",
                LocalDate.of(1999, 1, 1)));
        User testUser2 = userStorage.createUser(new User("abububaga@mail.ru", "Poka", "PokaPika",
                LocalDate.of(1998, 1, 1)));

        assertEquals(List.of(), filmStorage.getCommonFilms(testUser.getId(), testUser2.getId()));
    }
    @Test
    void getCommonFilmsIfTwoFilmCommon() {
        Film testFilm1 = filmStorage.createFilm(new Film("testFilm1",
                "testFilm1",
                LocalDate.of(2022, 1, 1),
                200, new Mpa(1, "G"), 1));
        Film testFilm2 = filmStorage.createFilm(new Film("testFilm2",
                "testFilm2", LocalDate.of(2023, 1, 1),
                200, new Mpa(1, "G"), 1));
        Film testFilm3 = filmStorage.createFilm(new Film("testFilm3",
                "testFilm3", LocalDate.of(2023, 1, 1),
                200, new Mpa(1, "G"), 1));

        User testUser = userStorage.createUser(new User("abububugaga@mail.ru", "Koka", "Kokapika",
                LocalDate.of(1999, 1, 1)));
        User testUser2 = userStorage.createUser(new User("abububaga@mail.ru", "Poka", "PokaPika",
                LocalDate.of(1998, 1, 1)));

        likeStorage.addLike(testFilm1.getId(), testUser.getId());
        likeStorage.addLike(testFilm1.getId(), testUser2.getId());
        likeStorage.addLike(testFilm2.getId(), testUser.getId());
        likeStorage.addLike(testFilm2.getId(), testUser2.getId());

        assertEquals(List.of(testFilm1, testFilm2), filmStorage.getCommonFilms(testUser.getId(), testUser2.getId()));
    }

    @Test
    void getCommonFilmsIfExistLikeFilmNotCommon() {
        Film testFilm1 = filmStorage.createFilm(new Film("testFilm1",
                "testFilm1",
                LocalDate.of(2022, 1, 1),
                200, new Mpa(1, "G"), 1));
        Film testFilm2 = filmStorage.createFilm(new Film("testFilm2",
                "testFilm2", LocalDate.of(2023, 1, 1),
                200, new Mpa(1, "G"), 1));

        User testUser = userStorage.createUser(new User("abububugaga@mail.ru", "Koka", "Kokapika",
                LocalDate.of(1999, 1, 1)));
        User testUser2 = userStorage.createUser(new User("abububaga@mail.ru", "Poka", "PokaPika",
                LocalDate.of(1998, 1, 1)));

        likeStorage.addLike(testFilm1.getId(), testUser.getId());
        likeStorage.addLike(testFilm2.getId(), testUser2.getId());

        assertEquals(List.of(), filmStorage.getCommonFilms(testUser.getId(), testUser2.getId()));
    }
}