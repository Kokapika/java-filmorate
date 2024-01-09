package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Positive(message = "ID Review must be positive")
    private Integer reviewId;

    @NotBlank(message = "Review description cannot be empty")
    private String content;

    @NotNull(message = "Review opinion cannot be empty")
    private Boolean isPositive;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer filmId;
    private Integer useful = 0;
}