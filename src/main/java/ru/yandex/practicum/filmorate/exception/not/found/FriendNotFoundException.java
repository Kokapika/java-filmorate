package ru.yandex.practicum.filmorate.exception.not.found;

public class FriendNotFoundException extends RuntimeException {
    public FriendNotFoundException(String message) {
        super(message);
    }
}
