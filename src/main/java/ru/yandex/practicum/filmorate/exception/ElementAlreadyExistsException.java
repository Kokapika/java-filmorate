package ru.yandex.practicum.filmorate.exception;

public class ElementAlreadyExistsException extends RuntimeException {
    public ElementAlreadyExistsException(String message) {
        super(message);
    }
}
