package ru.yandex.practicum.filmorate.exception.not.found;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
