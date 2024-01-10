package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    Director createDirector(Director director);

    Director getDirectorById(int id);

    List<Director> getAllDirectors();

    Director updateDirector(Director director);

    int deleteDirectorById(int id);

    void updateFilmDirectors(Integer id, List<Director> directors);

    List<Director> getFilmDirectorsById(Integer id);

    void deleteAllDirectorByFilmId(Integer id);
    boolean isDirectorExist(Integer directorId);
}
