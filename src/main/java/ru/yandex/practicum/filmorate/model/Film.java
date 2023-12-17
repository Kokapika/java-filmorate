package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Integer id;
    @NotEmpty
    private String name;
    @NotEmpty
    @Size(max = 200)
    private String description;
    @NotEmpty
    private LocalDate releaseDate;
    @Positive
    @NotEmpty
    private Integer duration;
    private Set<Genre> genres = new HashSet<>();
    @NotEmpty
    private Mpa mpa;
}
