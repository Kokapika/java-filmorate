package ru.yandex.practicum.filmorate.exception.not.found;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
