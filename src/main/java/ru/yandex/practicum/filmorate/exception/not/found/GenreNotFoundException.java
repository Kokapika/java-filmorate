package ru.yandex.practicum.filmorate.exception.not.found;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(String message) {
        super(message);
    }
}
