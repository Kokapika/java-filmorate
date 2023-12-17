//package ru.yandex.practicum.filmorate.controllerTests;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import ru.yandex.practicum.filmorate.controller.FilmController;
//import ru.yandex.practicum.filmorate.exception.ValidationException;
//import ru.yandex.practicum.filmorate.model.Film;
//import ru.yandex.practicum.filmorate.service.FilmService;
//import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
//import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
//import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
//import ru.yandex.practicum.filmorate.storage.user.UserStorage;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class FilmControllerValidationTest {
//    FilmController filmController;
//    FilmService filmService;
//    FilmStorage filmStorage;
//    UserStorage userStorage;
//
//    @BeforeEach
//    void beforeEach() {
//        filmStorage = new FilmDbStorage();
//        userStorage = new UserDbStorage();
//        filmService = new FilmService(filmStorage, userStorage);
//        filmController = new FilmController(filmService);
//    }
//
//    @Test
//    public void addFilm() {
//        Film film = new Film("Аватар", "Фантастика", LocalDate.of(2010, 10, 10), 200);
//        filmController.addFilm(film);
//
//        Film getFilm = filmController.getFilms().get(0);
//
//        assertNotNull(filmController.getFilms());
//        assertEquals("Аватар", getFilm.getName());
//        assertEquals("Фантастика", getFilm.getDescription());
//        assertEquals(LocalDate.of(2010, 10, 10), getFilm.getReleaseDate());
//        assertEquals(200, getFilm.getDuration());
//        assertEquals(1, getFilm.getId());
//    }
//
//    @Test
//    public void emptyFilmName() {
//        ValidationException exception = assertThrows(ValidationException.class, () -> {
//            Film film = new Film("", "Фантастика", LocalDate.of(2010, 10, 10), 200);
//            filmController.addFilm(film);
//        });
//
//        assertEquals("Название фильма не должно быть пустым", exception.getMessage());
//
//        ValidationException e = assertThrows(ValidationException.class, () -> {
//            Film film = new Film("   ", "Фантастика", LocalDate.of(2010, 10, 10), 200);
//            filmController.addFilm(film);
//        });
//
//        assertEquals("Название фильма не должно быть пустым", e.getMessage());
//    }
//
//    @Test
//    public void maxLengthFilmDescription() {
//        ValidationException exception = assertThrows(ValidationException.class, () -> {
//            Film film = new Film("Аватар", "«Аватар» рассказывает историю инвалида войны, бывшего морского пехотинца Джейка Салли," +
//                    " прибывшего на планету Пандора, чтобы работать на ресурсодобывающую мегакорпорацию. Салли получает в своё распоряжение аватара — тело," +
//                    " которым он может управлять, находясь в трансе. Ему удаётся подружиться с разумными жителями планеты, народом На’ви." +
//                    " В конце концов Джейк становится военным вождём племени и объявляет корпорации войну.",
//                    LocalDate.of(2010, 10, 10), 200);
//            filmController.addFilm(film);
//        });
//
//        assertEquals("Длина описания фильма не должна превышать 200 символов", exception.getMessage());
//    }
//
//    @Test
//    public void emptyFilmDescription() {
//        ValidationException e = assertThrows(ValidationException.class, () -> {
//            Film film = new Film("Аватар", "", LocalDate.of(2010, 10, 10), 200);
//            filmController.addFilm(film);
//        });
//
//        assertEquals("Описание фильма не должно быть пустым", e.getMessage());
//
//        ValidationException exception = assertThrows(ValidationException.class, () -> {
//            Film film = new Film("Аватар", "   ", LocalDate.of(2010, 10, 10), 200);
//            filmController.addFilm(film);
//        });
//
//        assertEquals("Описание фильма не должно быть пустым", exception.getMessage());
//    }
//
//    @Test
//    public void filmReleaseDate() {
//        Film f = new Film("Аватар", "Фантастика", LocalDate.of(1895, 12, 28), 200);
//        filmController.addFilm(f);
//
//        assertNotNull(filmController.getFilms());
//
//        ValidationException exception = assertThrows(ValidationException.class, () -> {
//            Film film = new Film("Аватар", "Фантастика", LocalDate.of(1895, 12, 27), 200);
//            filmController.addFilm(film);
//        });
//
//        assertEquals("Дата релиза фильма не должна быть раньше 28 декабря 1895 года", exception.getMessage());
//    }
//
//    @Test
//    public void negativeDurationFilm() {
//        ValidationException exception = assertThrows(ValidationException.class, () -> {
//            Film film = new Film("Аватар", "Фантастика", LocalDate.of(1895, 12, 29), -200);
//            filmController.addFilm(film);
//        });
//
//        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
//
//        ValidationException e = assertThrows(ValidationException.class, () -> {
//            Film film = new Film("Аватар", "Фантастика", LocalDate.of(1895, 12, 29), 0);
//            filmController.addFilm(film);
//        });
//
//        assertEquals("Продолжительность фильма должна быть положительной", e.getMessage());
//    }
//}
