package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDbService {
    private final UserStorage userStorage;
    private final FriendStorage friendsStorage;

    public User addUser(User user) {
        return userStorage.addUser(user).toBuilder().friends(friendsStorage.getFriends(user.getId())).build();
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user).toBuilder().friends(friendsStorage.getFriends(user.getId())).build();
    }

    public List<User> getUsers() {
        return userStorage.getUsers().stream()
                .peek(u -> u.setFriends(friendsStorage.getFriends(u.getId())))
                .collect(Collectors.toList());
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id).toBuilder().friends(friendsStorage.getFriends(id)).build();
    }

    public void deleteUser(Integer id) {
        userStorage.deleteUser(id);
    }
}
