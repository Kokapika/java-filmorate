package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserDbService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserDbService userDbService;

    protected void clearUsers() {
        userDbService.deleteUsers();
    }

    @GetMapping
    public List<User> getUsers() {
        return userDbService.getUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        checkUser(user);
        return userDbService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        checkUser(user);
        return userDbService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public int deleteUser(@PathVariable("id") int id) {
        return userDbService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") int id) {
        return userDbService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userDbService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userDbService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Integer id) {
        return userDbService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {
        return userDbService.getCommonFriends(id, otherId);
    }

    private void checkUser(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
    }
}
