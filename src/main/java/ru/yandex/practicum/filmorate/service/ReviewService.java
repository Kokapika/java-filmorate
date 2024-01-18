package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.user_event.EventType;
import ru.yandex.practicum.filmorate.model.user_event.OperationType;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class ReviewService {
    public static final int DOWN_RATING = -1;
    public static final int UP_RATING = 1;
    private final ReviewStorage reviewStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final UserEventService userEventService;

    @Autowired
    public ReviewService(ReviewStorage reviewStorage, FilmStorage filmStorage, UserStorage userStorage,
                         UserEventService userEventService) {
        this.reviewStorage = reviewStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.userEventService = userEventService;
    }

    public Review createReview(Review review) {
        if (!filmStorage.isFilmExist(review.getFilmId())) {
            throw new NotFoundException("Film not exist");
        }
        if (!userStorage.isExistingUser(review.getUserId())) {
            throw new NotFoundException("User not exist");
        }
        Review createdReview = reviewStorage.createReview(review);
        userEventService.addEvent(EventType.REVIEW, review.getUserId(), review.getReviewId(), OperationType.ADD);
        return createdReview;
    }

    public Review updateReview(Review review) {
        Review updatedReview = reviewStorage.updateReview(review);
        userEventService.addEvent(EventType.REVIEW, updatedReview.getUserId(), review.getReviewId(), OperationType.UPDATE);
        return updatedReview;
    }

    public Integer deleteReviewById(int id) {
        Integer userId = getReviewById(id).getUserId();
        Integer deleteResult = reviewStorage.deleteReviewById(id);
        if (deleteResult > 0) {
            userEventService.addEvent(EventType.REVIEW, userId, id, OperationType.REMOVE);
        }
        return deleteResult;
    }

    public Review getReviewById(int id) {
        return reviewStorage.getReviewById(id)
                .orElseThrow(() -> new NotFoundException("Review with id " + id + " does not exist."));
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