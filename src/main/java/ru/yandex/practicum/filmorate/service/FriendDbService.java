package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.already.added.FriendAlreadyAddedException;
import ru.yandex.practicum.filmorate.exception.not.found.FriendNotFoundException;
import ru.yandex.practicum.filmorate.model.Status;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendDbService {
    private final UserDbService userDbService;
    private final FriendStorage friendsStorage;

    public void addFriend(Integer id, Integer friendId) {
        User user = userDbService.getUserById(id);
        User friend = userDbService.getUserById(friendId);
        boolean isUserRequest = friendsStorage.getFriends(id).containsKey(friendId);
        boolean isFriendRequest = friendsStorage.getFriends(friendId).containsKey(id);

        if (isUserRequest && isFriendRequest)
            throw new FriendAlreadyAddedException(String.format("%s добавлен в друзья к %s.",
                    friend.getName(), user.getName()));
        else if (isUserRequest)
            throw new FriendAlreadyAddedException(String.format("%s уже подал заявку в друзья к %s.",
                    friend.getName(), user.getName()));
        else if (isFriendRequest) {
            friendsStorage.addFriend(id, friendId, Status.ACCEPTED);
            friendsStorage.updateStatus(friendId, id, Status.ACCEPTED);
        } else
            friendsStorage.addFriend(id, friendId, Status.NOT_ACCEPTED);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        HashMap<Integer, Status> userFriends = friendsStorage.getFriends(id);
        if (!userFriends.containsKey(friendId))
            throw new FriendNotFoundException(String.format("%s не является другом %s, поэтому не может быть удалён.",
                    userDbService.getUserById(id).getName(), userDbService.getUserById(friendId).getName()));
        else if (userFriends.get(friendId).equals(Status.ACCEPTED))
            friendsStorage.updateStatus(friendId, id, Status.NOT_ACCEPTED);

        friendsStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(Integer id) {
        return friendsStorage.getFriends(id).keySet().stream()
                .map(userDbService::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        List<User> commonFriends = getFriends(id);
        commonFriends.retainAll(getFriends(otherId));
        return commonFriends;
    }
}