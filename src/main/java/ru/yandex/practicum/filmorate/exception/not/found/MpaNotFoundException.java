package ru.yandex.practicum.filmorate.exception.not.found;

public class MpaNotFoundException extends RuntimeException {
    public MpaNotFoundException(String message) {
        super(message);
    }
}
