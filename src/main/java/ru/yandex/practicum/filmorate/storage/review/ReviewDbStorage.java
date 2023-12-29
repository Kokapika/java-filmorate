package ru.yandex.practicum.filmorate.storage.review;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ReviewDbStorage implements ReviewStorage {
    public static final int DOWN_RATING = -1;
    public static final int UP_RATING = 1;
    private final JdbcTemplate jdbcTemplate;

    private Review mapToReview(ResultSet rs, int rowNum) throws SQLException {
        System.out.println(rs.getInt("review_id"));
        return Review.builder()
                .reviewId(rs.getInt("review_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .useful(getReviewRank(rs.getInt("review_id")))
                .build();
    }

    private Integer getReviewRank(int reviewId) {
        String query = "SELECT COALESCE(SUM(estimation), 0) FROM estimation_review WHERE review_id = ?";
        return jdbcTemplate.queryForObject(query, Integer.class, reviewId);
    }

    private Map<String, Object> reviewToMap(Review review) {
        Map<String, Object> values = new HashMap<>();
        values.put("content", review.getContent());
        values.put("is_positive", review.getIsPositive());
        values.put("user_id", review.getUserId());
        values.put("film_id", review.getFilmId());
        return values;
    }

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Review createReview(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("review")
                .usingGeneratedKeyColumns("review_id");
        Number key = simpleJdbcInsert.executeAndReturnKey(reviewToMap(review));
        review.setReviewId((Integer) key);
        log.info("Review with ID {} saved.", review.getReviewId());
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        String query = "UPDATE review SET content=?, is_positive=? WHERE review_id=?";
        int reviewId = review.getReviewId();
        int updateResult = jdbcTemplate.update(query,
                review.getContent(),
                review.getIsPositive(),
                reviewId);
        if (updateResult > 0) {
            log.info("Review with ID {} has been updated.", reviewId);
        } else {
            throw new NotFoundException("Review not founded for update by ID=" + reviewId);
        }
        return getReviewById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found for ID=" + reviewId));
    }

    @Override
    public Integer deleteReviewById(int reviewId) {
        String query = "DELETE FROM review WHERE review_id=?";
        int deleteResult = jdbcTemplate.update(query, reviewId);
        if (deleteResult > 0) {
            log.info("Review with ID {} has been removed.", reviewId);
        } else {
            log.info("Review with ID {} has not been deleted.", reviewId);
        }
        return deleteResult;
    }

    @Override
    public Optional<Review> getReviewById(int reviewId) {
        System.out.println(reviewId);
        String query = "SELECT * FROM review WHERE review_id =?";
        log.info("Review with ID {} returned.", reviewId);
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, this::mapToReview, reviewId));
    }

    @Override
    public List<Review> getAllReviewsByFilmId(Integer filmId, Integer count) {
        String query = "SELECT * FROM review WHERE film_id =?";
        log.info("Reviews for Film with ID {} returned.", filmId);
        return jdbcTemplate.query(query, this::mapToReview, filmId)
                .stream()
                .sorted(Comparator.comparingInt(Review::getUseful).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> getAllReviews() {
        String query = "SELECT * FROM review";
        log.info("All Reviews returned.");
        return jdbcTemplate.query(query, this::mapToReview)
                .stream()
                .sorted(Comparator.comparingInt(Review::getUseful).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Integer likeReview(int idReview, int idUser) {
        String query = "MERGE INTO estimation_review (user_id, review_id, estimation) " +
                "KEY(user_id, review_id) " +
                "VALUES (?, ?, ?)";
        int insertResult = jdbcTemplate.update(query, idUser, idReview, UP_RATING);
        if (insertResult > 0) {
            log.info("Like has added for review by ID {} from user by ID {}.", idReview, idUser);
        }
        return insertResult;
    }

    @Override
    public Integer dislikeReview(int idReview, int idUser) {
        if (idUser < 1) {
            throw new NotFoundException("User not exists by ID=" + idUser);
        }
        String query = "MERGE INTO estimation_review (user_id, review_id, estimation) " +
                "KEY(user_id, review_id) " +
                "VALUES (?, ?, ?)";
        int insertResult = jdbcTemplate.update(query, idUser, idReview, DOWN_RATING);
        if (insertResult > 0) {
            log.info("Like has changed to Dislike for review by ID {} from user by ID {}.", idReview, idUser);
        }
        return insertResult;
    }

    @Override
    public Integer removeLikeReview(int idReview, int idUser) {
        if (idUser < 1) {
            throw new NotFoundException("User not exists by ID=" + idUser);
        }
        String query = "DELETE FROM estimation_review WHERE review_id=? AND user_id=? AND estimation=?";
        int insertResult = jdbcTemplate.update(query, idReview, idUser, UP_RATING);
        if (insertResult > 0) {
            log.info("DisLike has removed for review by ID {} from user by ID {}.", idReview, idUser);
        }
        return insertResult;
    }

    @Override
    public Integer removeDislikeReview(int idReview, int idUser) {
        if (idUser < 1) {
            throw new NotFoundException("User not exists by ID=" + idUser);
        }
        String query = "DELETE FROM estimation_review WHERE review_id=? AND user_id=? AND estimation=?";
        int insertResult = jdbcTemplate.update(query, idReview, idUser, DOWN_RATING);
        if (insertResult > 0) {
            log.info("DisLike has removed for review by ID {} from user by ID {}.", idReview, idUser);
        }
        return insertResult;
    }
}