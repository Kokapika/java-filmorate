package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class ReviewDbService {
    private final ReviewStorage reviewStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public ReviewDbService(ReviewStorage reviewStorage, FilmStorage filmStorage, UserStorage userStorage) {
        this.reviewStorage = reviewStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Review createReview(Review review) {
        filmStorage.getFilmById(review.getFilmId());
        userStorage.getUserById(review.getUserId());
        return reviewStorage.createReview(review);
    }

    public Review updateReview(Review review) {
        return reviewStorage.updateReview(review);
    }

    public Integer deleteReviewById(int id) {
        return reviewStorage.deleteReviewById(id);
    }

    public Review getReviewById(int id) {
        try {
            return reviewStorage.getReviewById(id)
                    .orElseThrow(() -> new NotFoundException("Review with id " + id + " does not exist."));
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Review with id " + id + " does not exist.");
        }
    }

    public List<Review> getAllReviews(Integer filmId, Integer count) {
        if (filmId == null) {
            return reviewStorage.getAllReviews();
        }
        return reviewStorage.getAllReviewsByFilmId(filmId, count);
    }

    public Integer likeReview(int idReview, int idUser) {
        return reviewStorage.likeReview(idReview, idUser);
    }

    public Integer dislikeReview(int idReview, int idUser) {
        return reviewStorage.dislikeReview(idReview, idUser);
    }

    public Integer removeLikeReview(int idReview, int idUser) {
        return reviewStorage.removeLikeReview(idReview, idUser);
    }

    public Integer removeDislikeReview(int idReview, int idUser) {
        return reviewStorage.removeDislikeReview(idReview, idUser);
    }
}