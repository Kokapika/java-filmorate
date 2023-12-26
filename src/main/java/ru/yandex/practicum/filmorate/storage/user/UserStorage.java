package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    int addFriend(int id, int friendId);

    int deleteFriend(int userId, int friendId);

    User createUser(User user);

    User updateUser(User user);

    void deleteAllUsers();

    int deleteUserById(int id);

    List<User> getAllUsers();

    User getUserById(int id);

    List<User> getFriendsById(int id);

    List<User> getCommonFriends(int id, int friendId);
}
