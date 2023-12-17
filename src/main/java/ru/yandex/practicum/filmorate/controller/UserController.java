package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserDbService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserDbService userDbService;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Получен POST запрос на добавление пользователя");
        return userDbService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен PUT запрос на обновление пользователя");
        return userDbService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен PUT запрос на добавление в друзья");
        userDbService.addFriend(id, friendId);
        log.info("Пользователи с id " + id + " и " + friendId + " теперь друзья");
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        log.info("Получен GET запрос на получение пользователя");
        return userDbService.getUserById(id);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен GET запрос на получение пользователей");
        return userDbService.getUsers();
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        log.info("Получен GET запрос на получение списка друзей пользователя");
        return userDbService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Получен GET запрос на получение общего списка друзей");
        return userDbService.getCommonFriends(id, otherId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен DELETE запрос на удаление из друзей");
        userDbService.deleteFriend(id, friendId);
        log.info("Пользователи с id " + id + " и " + friendId + " теперь не друзья");
    }
}
