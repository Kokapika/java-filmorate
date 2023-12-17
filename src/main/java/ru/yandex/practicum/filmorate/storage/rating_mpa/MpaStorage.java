package ru.yandex.practicum.filmorate.storage.rating_mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    Mpa getMpaById(Integer mpaId);

    List<Mpa> getAllMpa();

    boolean containsMpa(Integer mpaId);
}
