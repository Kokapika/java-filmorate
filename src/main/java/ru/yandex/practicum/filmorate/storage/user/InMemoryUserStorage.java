package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.not.found.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int generateId = 1;

    @Override
    public User addUser(User user) {
        user.setId(generateId);
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(generateId++, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.debug("Обновлена информация о пользователя " + user.getName() + ", с id = " + user.getId());
        } else if (user.getId() == null) {
            user.setId(generateId++);
            users.put(user.getId(), user);
            log.debug("Добавлен новый пользователь " + user.getName());
        } else {
            log.debug("Не удалось обновить данные о пользователе");
            throw new NotFoundException("Неверный идентификатор пользователя");
        }
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        User user = users.get(id);
        if (user == null) {
            log.debug("Пользователь с id=" + id + " не найден");
            throw new NotFoundException("Неверный идентификатор пользователя");
        }
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUser(Integer id) {
        users.remove(id);
    }
}
