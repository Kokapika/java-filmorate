package ru.yandex.practicum.filmorate.model.user_event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEvent {
    @Positive
    @NotNull
    private Integer eventId;

    @NotNull
    private EventType eventType;

    @NotNull
    @Positive
    private Integer userId;

    @NotNull
    private Integer entityId;

    @NotNull
    @Positive
    private Long timestamp;

    @NotNull
    private OperationType operation;
}