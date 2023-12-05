package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Получен POST запрос на добавление пользователя");
        validationUser(user);
        return userService.getUserStorage().addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен PUT запрос на обновление пользователя");
        validationUser(user);
        return userService.getUserStorage().updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен PUT запрос на добавление в друзья");
        userService.addFriends(id, friendId);
        log.info("Пользователи с id " + id + " и " + friendId + " теперь друзья");
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        log.info("Получен GET запрос на получение пользователя");
        return userService.getUserStorage().getUserById(id);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен GET запрос на получение пользователей");
        return userService.getUserStorage().getUsers();
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable Integer id) {
        log.info("Получен GET запрос на получение списка друзей пользователя");
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Получен GET запрос на получение общего списка друзей");
        return userService.getCommonFriends(id, otherId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен DELETE запрос на удаление из друзей");
        userService.deleteFriends(id, friendId);
        log.info("Пользователи с id " + id + " и " + friendId + " теперь не друзья");
    }

    private User validationUser(User user) {
        if (user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            log.error("Валидация при добавлении/обновлении пользователя не пройдена");
            throw new ValidationException("Почта не может быть пустой");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Валидация при добавлении/обновлении пользователя не пройдена");
            throw new ValidationException("Почта должна содержать символ '@'");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Валидация при добавлении/обновлении пользователя не пройдена");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Валидация при добавлении/обновлении пользователя не пройдена");
            throw new ValidationException("Дата рождения не может быть в будушем");
        }
        return user;
    }
}
