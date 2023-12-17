package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.rating_mpa.MpaDao;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaDbService {
    private final MpaDao mpaDao;

    public Mpa getMpaById(Integer mpaId) {
        if (!mpaDao.containsMpa(mpaId)) {
            throw new InvalidIdException("Рейтинг фильма с id = " + mpaId + " не существует");
        }
        Mpa result = mpaDao.getMpaById(mpaId);
        log.debug("Получен рейтинг фильма с id = " + mpaId);
        return result;
    }

    public List<Mpa> getAllMpa() {
        List<Mpa> result = mpaDao.getAllMpa();
        log.debug("Получены все рейтинги фильмов");
        return result;
    }
}
