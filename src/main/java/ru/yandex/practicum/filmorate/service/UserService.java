package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public void addFriends(Integer id, Integer friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
    }

    public void deleteFriends(Integer id, Integer friendId) {
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
    }

    public List<User> getAllFriends(Integer id) {
        User user = userStorage.getUserById(id);
        Set<Integer> friendsId = user.getFriends();
        List<User> friends = new ArrayList<>();
        for (Integer friend : friendsId) {
            friends.add(userStorage.getUserById(friend));
        }
        return friends;
    }

    public Set<User> getCommonFriends(Integer id, Integer otherId) {
        User user = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(otherId);
        Set<Integer> userFriends = new HashSet<>(user.getFriends());
        Set<Integer> otherUserFriends = new HashSet<>(otherUser.getFriends());
        Set<Integer> commonFriendsId = userFriends.stream().filter(otherUserFriends::contains).collect(Collectors.toSet());
        Set<User> commonFriends = new HashSet<>();
        for (Integer friendId : commonFriendsId) {
            commonFriends.add(userStorage.getUserById(friendId));
        }
        return commonFriends;
    }
}
