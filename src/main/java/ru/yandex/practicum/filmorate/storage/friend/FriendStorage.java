package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.Status;

import java.util.HashMap;

public interface FriendStorage {
    void addFriend(Integer id, Integer friendId, Status status);

    void updateStatus(Integer id, Integer friendId, Status status);

    HashMap<Integer, Status> getFriends(Integer id);

    void deleteFriend(Integer id, Integer friendId);
}
