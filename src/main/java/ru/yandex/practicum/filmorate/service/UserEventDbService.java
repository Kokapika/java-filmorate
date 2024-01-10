package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user_event.EventType;
import ru.yandex.practicum.filmorate.model.user_event.OperationType;
import ru.yandex.practicum.filmorate.model.user_event.UserEvent;
import ru.yandex.practicum.filmorate.storage.user_event.UserEventStorage;

import java.util.List;

@Service
public class UserEventDbService {
    private final UserEventStorage userEventStorage;
    private final UserDbService userDbService;

    @Autowired
    public UserEventDbService(UserEventStorage userEventStorage, @Lazy UserDbService userDbService) {
        this.userEventStorage = userEventStorage;
        this.userDbService = userDbService;
    }

    public void addEvent(EventType eventType, int userId, int entityId, OperationType operationType) {
        userEventStorage.addEvent(eventType, userId, entityId, operationType);
    }

    public List<UserEvent> getEventsByUserId(int userId) {
        if (!userDbService.isExistingUser(userId)) {
            throw new NotFoundException("Not founded user by ID:" + userId);
        }
        return userEventStorage.getEventsByUserId(userId);
    }
}