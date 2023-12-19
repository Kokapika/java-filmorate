package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    NOT_ACCEPTED(1, "ожидается"),
    ACCEPTED(2, "принято");

    private final Integer statusId;
    private final String name;
}
