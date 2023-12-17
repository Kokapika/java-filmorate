package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipStorage {
    Friendship addFriend(Integer fromUserId, Integer toUserId, boolean isAgree);

    void deleteFriend(Integer fromUserId, Integer toUserId);

    List<Integer> getFriends(Integer toUserId);

    boolean containsFriendship(Integer fromUserId, Integer toUserId);
}
