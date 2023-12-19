package ru.yandex.practicum.filmorate.storage.like;

import java.util.List;
import java.util.Set;

public interface LikeStorage {
    void addLike(Integer filmId, Integer userId);

    Set<Integer> getLikes(Integer filmId);

    void deleteLike(Integer filmId, Integer userId);

    List<Integer> getPopularFilms(Integer count);
}
