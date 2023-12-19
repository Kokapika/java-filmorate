package ru.yandex.practicum.filmorate.exception.not.found;

public class FilmLikeNotFoundException extends RuntimeException {
    public FilmLikeNotFoundException(String message) {
        super(message);
    }
}
