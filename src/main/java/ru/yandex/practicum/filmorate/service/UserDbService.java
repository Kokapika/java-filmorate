package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDbService {
    private final UserStorage userStorage;

    public User addUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteUsers() {
        userStorage.deleteAllUsers();
    }

    public int deleteUser(int id) {
        return userStorage.deleteUserById(id);
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
    }

    public void deleteFriend(int id, int friendId) {
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(int id) {
        return userStorage.getFriendsById(id);
    }

    public List<User> getCommonFriends(int id, int otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}