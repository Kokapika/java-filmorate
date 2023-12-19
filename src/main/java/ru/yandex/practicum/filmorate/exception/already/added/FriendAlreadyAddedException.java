package ru.yandex.practicum.filmorate.exception.already.added;

public class FriendAlreadyAddedException extends RuntimeException {
    public FriendAlreadyAddedException(String message) {
        super(message);
    }
}
