package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friendship {
    @NotEmpty
    private Integer fromUserId;
    @NotEmpty
    private Integer toUserId;
    @NotEmpty
    private boolean isAgree;
}
