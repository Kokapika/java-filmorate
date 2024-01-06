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
    public static final int DOWN_RATING = -1;
    public static final int UP_RATING = 1;
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
        return reviewStorage.getSortedReviews(filmId, count);
    }

    public Integer likeReview(int reviewId, int userId) {
        return reviewStorage.changeReviewEstimation(reviewId, userId, UP_RATING);
    }

    public Integer dislikeReview(int reviewId, int userId) {
        return reviewStorage.changeReviewEstimation(reviewId, userId, DOWN_RATING);
    }

    public Integer removeReviewEstimation(int reviewId, int userId) {
        return reviewStorage.removeReviewEstimation(reviewId, userId);
    }
}