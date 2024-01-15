package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.user_event.EventType;
import ru.yandex.practicum.filmorate.model.user_event.OperationType;
import ru.yandex.practicum.filmorate.model.user_event.UserEvent;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final UserEventService userEventService;

    public User addUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public int deleteUser(int id) {
        if (userStorage.isExistingUser(id))
            return userStorage.deleteUserById(id);
        else
            throw new NotFoundException(String.format("Пользователь %d не может быть найден", id));
    }

    public List<User> getUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public void addFriend(Integer id, Integer friendId) {
        if (id < 0 || friendId < 0) {
            throw new NotFoundException("Пользователь не может быть добавлен");
        }
        userStorage.addFriend(id, friendId);
        userEventService.addEvent(EventType.FRIEND, id, friendId, OperationType.ADD);
    }

    public void deleteFriend(int id, int friendId) {
        userStorage.deleteFriend(id, friendId);
        userEventService.addEvent(EventType.FRIEND, id, friendId, OperationType.REMOVE);
    }

    public List<User> getFriends(int id) {
        if (userStorage.isExistingUser(id))
            return userStorage.getFriendsById(id);
        else
            throw new NotFoundException(String.format("Пользователь %d не может быть найден", id));
    }

    public List<User> getCommonFriends(int id, int otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }

    public boolean isExistingUser(int userId) {
        return userStorage.isExistingUser(userId);
    }

    public boolean isFriend(int userId, int friendId) {
        return userStorage.isFriend(userId, friendId);
    }

    public List<UserEvent> getUserEvents(Integer id) {
        return userEventService.getEventsByUserId(id);
    }
}