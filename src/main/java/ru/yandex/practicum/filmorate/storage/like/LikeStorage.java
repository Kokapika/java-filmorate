package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.FilmLikes;

import java.util.List;

public interface LikeStorage {
    int addLike(int filmId, int userId);

    int deleteLike(int filmId, int userId);

    List<FilmLikes> getAllLikes();
}
