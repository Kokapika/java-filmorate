package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String login;
    private String name;
    @Past
    @NotEmpty
    private LocalDate birthday;

    public String getName() {
        if (name == null || name.isBlank()) {
            name = login;
        }
        return name;
    }
}
