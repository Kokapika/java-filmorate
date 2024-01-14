package ru.yandex.practicum.filmorate.storage.review;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReviewDbStorageTest {
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private ReviewDbStorage reviewStorage;
    private Film film;
    private User user;
    private Review badReview;

    private void createReviews() {
        badReview = Review.builder()
                .content("Bad film")
                .isPositive(false)
                .userId(1)
                .filmId(1)
                .useful(0)
                .build();
        film = Film.builder()
                .name("Some Film")
                .releaseDate(LocalDate.of(2008, 11, 1))
                .description("This is description")
                .duration(120)
                .mpa(new Mpa())
                .genres(new ArrayList<>())
                .build();
        user = User.builder()
                .name("Vasya")
                .email("mail@mail.ru")
                .login("Vasvas")
                .birthday(LocalDate.of(2000, 8, 6))
                .build();
    }

    @BeforeEach
    void setUp() {
        createReviews();
        userStorage.createUser(user);
        filmStorage.createFilm(film);
    }

    @Test
    void createReview() {
        Review review = reviewStorage.createReview(badReview);

        Optional<Review> reviewById = reviewStorage.getReviewById(review.getReviewId());

        assertThat(reviewById.get()).isEqualTo(review);
    }

    @Test
    void updateReview() {
        Review review = reviewStorage.createReview(badReview);
        review.setIsPositive(false);
        review.setContent("Bad film");

        Review updatedReview = reviewStorage.updateReview(review);

        assertThat(updatedReview).isEqualTo(review);
    }

    @Test
    void deleteReviewById() {
        Review review = reviewStorage.createReview(badReview);

        Integer result = reviewStorage.deleteReviewById(1);

        List<Review> allReviews = reviewStorage.getAllReviews();

        assertTrue(result > 0);
        assertThat(allReviews).isEmpty();
    }

    @Test
    void getReviewById() {
        Review review = reviewStorage.createReview(badReview);
        Integer reviewId = review.getReviewId();

        Optional<Review> returnedReview = reviewStorage.getReviewById(reviewId);

        assertThat(returnedReview.get()).isEqualTo(review);
    }

    @Test
    void getAllReviewsByFilmId() {
        Review review = reviewStorage.createReview(badReview);

        List<Review> allReviewsByFilmId = reviewStorage.getSortedReviews(1, 10);

        assertThat(allReviewsByFilmId.get(0)).isEqualTo(review);
    }

    @Test
    void getAllReviews() {
        Review review = reviewStorage.createReview(badReview);

        List<Review> allReviews = reviewStorage.getAllReviews();

        assertThat(allReviews.get(0)).isEqualTo(review);
    }

    @Test
    void likeReview() {
        Review review = reviewStorage.createReview(badReview);

        Integer i = reviewStorage.changeReviewEstimation(1, 1, 1);

        Optional<Review> reviewById = reviewStorage.getReviewById(review.getReviewId());

        assertThat(reviewById.get().getUseful()).isEqualTo(1);
    }

    @Test
    void dislikeReview() {
        Review review = reviewStorage.createReview(badReview);

        Integer i = reviewStorage.changeReviewEstimation(1, 1, -1);

        Optional<Review> reviewById = reviewStorage.getReviewById(review.getReviewId());

        assertThat(reviewById.get().getUseful()).isEqualTo(-1);
    }

    @Test
    void removeLikeReview() {
        Review review = reviewStorage.createReview(badReview);
        Integer i = reviewStorage.changeReviewEstimation(1, 1, 1);
        Optional<Review> reviewById = reviewStorage.getReviewById(review.getReviewId());
        assertThat(reviewById.get().getUseful()).isEqualTo(1);

        reviewStorage.removeReviewEstimation(1, 1);
        Optional<Review> reviewAfterRemoveLike = reviewStorage.getReviewById(review.getReviewId());

        assertThat(reviewAfterRemoveLike.get().getUseful()).isEqualTo(0);
    }

    @Test
    void removeDislikeReview() {
        Review review = reviewStorage.createReview(badReview);
        Integer i = reviewStorage.changeReviewEstimation(1, 1, -1);
        Optional<Review> reviewById = reviewStorage.getReviewById(review.getReviewId());
        assertThat(reviewById.get().getUseful()).isEqualTo(-1);

        reviewStorage.removeReviewEstimation(1, 1);
        Optional<Review> reviewAfterRemoveDislike = reviewStorage.getReviewById(review.getReviewId());

        assertThat(reviewAfterRemoveDislike.get().getUseful()).isEqualTo(0);
    }
}