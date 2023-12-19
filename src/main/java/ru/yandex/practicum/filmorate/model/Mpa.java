package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Mpa implements Serializable {
    G(1, "G"),
    PG(2, "PG"),
    PG13(3, "PG-13"),
    R(4, "R"),
    NC17(5, "NC-17");

    private final Integer id;
    private final String name;

    @JsonCreator
    public static Mpa forValues(@JsonProperty("id") Integer id) {
        for (Mpa mpa : Mpa.values()) {
            if (mpa.id == id)
                return mpa;
        }
        return null;
    }
}
