package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String s) {
        super(s);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
