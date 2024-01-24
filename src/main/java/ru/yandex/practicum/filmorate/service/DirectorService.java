package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public List<Director> getDirectors() {
        return directorStorage.getAllDirectors();
    }

    public Director getDirectorById(int id) {
        return directorStorage.getDirectorById(id);
    }

    public Director addDirector(Director director) {
        return directorStorage.createDirector(director);
    }

    public Director updateDirector(Director director) {
        return directorStorage.updateDirector(director);
    }

    public int deleteDirectorById(Integer id) {
        return directorStorage.deleteDirectorById(id);
    }
}
