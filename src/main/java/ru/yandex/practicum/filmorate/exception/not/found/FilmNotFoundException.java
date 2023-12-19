package ru.yandex.practicum.filmorate.exception.not.found;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(String message) {
        super(message);
    }
}
