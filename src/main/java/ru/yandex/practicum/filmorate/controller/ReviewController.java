package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public Review createReview(@Valid @RequestBody Review review) {
        log.info("POST request received to add review:" + review);
        return reviewService.createReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        log.info("PUT request received to update review:" + review);
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{id}")
    public Integer deleteReviewById(@PathVariable int id) {
        if (id < 1) {
            throw new NotFoundException("User not exists by ID=" + id);
        }
        log.info("DELETE request received to remove review by id:" + id);
        return reviewService.deleteReviewById(id);
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable int id) {
        log.info("GET request received to get review by id:" + id);
        return reviewService.getReviewById(id);
    }

    @GetMapping
    public List<Review> getAllReviews(@RequestParam(value = "filmId", required = false) Integer filmId,
                                      @RequestParam(value = "count", defaultValue = "10") Integer count) {
        log.info("GET request received to get all review by Film id:" + filmId);
        return reviewService.getAllReviews(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public Integer likeReview(@PathVariable int id, @PathVariable int userId) {
        log.info("PUT request received to like review by id:" + id);
        return reviewService.likeReview(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public Integer dislikeReview(@PathVariable int id, @PathVariable int userId) {
        log.info("PUT request received to dislike review by id:" + id);
        return reviewService.dislikeReview(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Integer removeLikeReview(@PathVariable int id, @PathVariable int userId) {
        log.info("DELETE request received to remove like for review by id:" + id);
        return reviewService.removeReviewEstimation(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public Integer removeDislikeReview(@PathVariable int id, @PathVariable int userId) {
        log.info("DELETE request received to remove dislike for review by id:" + id);
        return reviewService.removeReviewEstimation(id, userId);
    }
}