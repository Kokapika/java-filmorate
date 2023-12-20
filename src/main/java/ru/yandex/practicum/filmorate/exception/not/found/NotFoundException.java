package ru.yandex.practicum.filmorate.exception.not.found;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
