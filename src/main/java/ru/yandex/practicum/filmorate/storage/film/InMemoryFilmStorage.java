package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int generateId = 1;

    @Override
    public Film addFilm(Film film) {
        film.setId(generateId);
        films.put(generateId++, film);
        log.debug("Добавлен новый фильм " + film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Обновлена информация о фильме " + film.getName() + ", с id = " + film.getId());
        } else if (film.getId() == null) {
            film.setId(generateId++);
            films.put(film.getId(), film);
            log.debug("Добавлен новый фильм " + film.getName());
        } else {
            log.debug("Не удалось обновить данные о фильме");
            throw new InvalidIdException("Неверный идентификатор фильма");
        }
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        Film film = films.get(id);
        if (film == null) {
            log.debug("Фильм с id=" + id + " не найден");
            throw new InvalidIdException("Неверный идентификатор фильма");
        }
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
