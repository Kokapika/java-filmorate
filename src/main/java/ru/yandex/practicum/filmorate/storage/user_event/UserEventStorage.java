package ru.yandex.practicum.filmorate.storage.user_event;

import ru.yandex.practicum.filmorate.model.user_event.EventType;
import ru.yandex.practicum.filmorate.model.user_event.OperationType;
import ru.yandex.practicum.filmorate.model.user_event.UserEvent;

import java.util.List;

public interface UserEventStorage {

    void addEvent(EventType eventType, int userId, int entityId, OperationType operationType);

    List<UserEvent> getEventsByUserId(int userId);
}