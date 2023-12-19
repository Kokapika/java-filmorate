package ru.yandex.practicum.filmorate.exception.already.added;

public class FilmLikeAlreadyAddedException extends RuntimeException {
    public FilmLikeAlreadyAddedException(String message) {
        super(message);
    }
}
