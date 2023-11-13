package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int generateId = 1;
    private final static Logger log = LoggerFactory.getLogger(User.class);

    @PostMapping
    public User addUser(@RequestBody User user) {
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
    public User updateUser(@RequestBody User user) {
        log.info("Получен PUT запрос на обновление пользователя");
        validationUser(user);
        User u = users.get(user.getId());
        u.setName(user.getName());
        u.setLogin(user.getLogin());
        u.setEmail(user.getEmail());
        u.setBirthday(user.getBirthday());
        return u;
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
