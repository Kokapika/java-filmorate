package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDao;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDbService {
    private final UserStorage userStorage;
    private final FriendshipDao friendshipDao;

    public User addUser(User user) {
        if (userStorage.containsUser(user.getId())) {
            throw new ElementAlreadyExistsException("Пользователь уже существует");
        }
        User result = userStorage.addUser(user);
        log.debug("В хранилище добавлен пользователь: " + user.getName());
        return result;
    }

    public User updateUser(User user) {
        if (!userStorage.containsUser(user.getId())) {
            throw new EmptyResultException("Пользователь в хранилище не существует");
        }
        User result = userStorage.updateUser(user);
        log.debug("Обновлены данные пользователя с id = " + user.getId());
        return result;
    }

    public User getUserById(Integer userId) {
        if (!userStorage.containsUser(userId)) {
            throw new InvalidIdException("Пользователя с id = " + userId + " не существует");
        }
        User user = userStorage.getUserById(userId);
        log.debug("Получен пользователь с id = " + userId);
        return user;
    }

    public List<User> getUsers() {
        List<User> users = userStorage.getUsers();
        log.debug("Получены все пользователи");
        return users;
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (!userStorage.containsUser(userId)) {
            throw new InvalidIdException("Пользователя c id = " + userId + " не существует");
        }
        if (!userStorage.containsUser(friendId)) {
            throw new InvalidIdException("Пользователя с id = " + friendId + " не существует");
        }
        if (userId == friendId) {
            throw new WrongLogicException("Пользователь не может добавить сам себя в друзья");
        }
        if (friendshipDao.containsFriendship(friendId, userId)) {
            throw new ElementAlreadyExistsException("Пользователь уже отправлял заявку на дружбу");
        }
        boolean isAgree = friendshipDao.containsFriendship(friendId, userId);
        friendshipDao.addFriend(friendId, userId, isAgree);
        log.debug("Пользователь с id = " + userId + " отправил заявку в друзья пользователю с id = " + friendId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        if (!userStorage.containsUser(userId)) {
            throw new InvalidIdException("Пользователя c id = " + userId + " не существует");
        }
        if (!userStorage.containsUser(friendId)) {
            throw new InvalidIdException("Пользователя с id = " + friendId + " не существует");
        }
        if (userId == friendId) {
            throw new WrongLogicException("Пользователь не может удалить сам себя из друзей");
        }
        if (!friendshipDao.containsFriendship(friendId, userId)) {
            throw new EmptyResultException("Пользователя с id = " + friendId + " в друзьях нет");
        }
        friendshipDao.deleteFriend(friendId, userId);
        log.debug("Пользователь с id = " + userId + " удалил из друзей пользователя с id = " + friendId);
    }

    public List<User> getFriends(Integer userId) {
        if (!userStorage.containsUser(userId)) {
            throw new InvalidIdException("Пользователя с id = " + userId + " не существует");
        }
        List<User> friends = friendshipDao.getFriends(userId).stream()
                .mapToInt(Integer::valueOf)
                .mapToObj(userStorage::getUserById)
                .collect(Collectors.toList());
        log.debug("Получены друзья пользователя с id " + userId);
        return friends;
    }

    public List<User> getCommonFriends(Integer userId, Integer otherUserId) {
        if (!userStorage.containsUser(userId)) {
            throw new EmptyResultException("Пользователя с id = " + userId + " не существует");
        }
        if (!userStorage.containsUser(otherUserId)) {
            throw new EmptyResultException("Пользователя с id = " + otherUserId + " не существует");
        }
        if (userId == otherUserId) {
            throw new WrongLogicException("Нельзя запросить общих друзей у себя же");
        }
        List<User> commonFriends = CollectionUtils.intersection(
                        friendshipDao.getFriends(userId),
                        friendshipDao.getFriends(otherUserId)).stream()
                .mapToInt(Integer::valueOf)
                .mapToObj(userStorage::getUserById)
                .collect(Collectors.toList());
        log.debug("Получен список общих друзей пользователей");
        return commonFriends;
    }
}
