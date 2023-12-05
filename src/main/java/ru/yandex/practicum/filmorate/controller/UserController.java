package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidIdException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int generateId = 1;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Получен POST запрос на добавление пользователя");
        validationUser(user);
        user.setId(generateId);
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(generateId++, user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен PUT запрос на обновление пользователя");
        validationUser(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Обновлена информация о пользователя " + user.getName() + ", с id = " + user.getId());
        } else if (user.getId() == null) {
            user.setId(generateId++);
            users.put(user.getId(), user);
            log.debug("Добавлен новый пользователь " + user.getName());
        } else {
            log.debug("Не удалось обновить данные о пользователе");
            throw new InvalidIdException("Неверный идентификатор пользователя");
        }
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен GET запрос на получение пользователей");
        return new ArrayList<>(users.values());
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
