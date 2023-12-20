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

    @GetMapping
    public List<User> getUsers() {
        return userDbService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userDbService.getUserById(id);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        checkUser(user);
        return userDbService.addUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userDbService.deleteUser(id);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        checkUser(user);
        return userDbService.updateUser(user);
    }

    private void checkUser(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
    }
}
