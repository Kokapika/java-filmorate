package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EmptyResultException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.*;

import static java.lang.String.format;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        jdbcTemplate.update(""
                        + "INSERT INTO users (email, login, name, birthday) "
                        + "VALUES (?, ?, ?, ?)",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()));
        return jdbcTemplate.queryForObject(format(""
                + "SELECT user_id, email, login, name, birthday "
                + "FROM users "
                + "WHERE email='%s'", user.getEmail()), new UserMapper());
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(""
                        + "UPDATE users "
                        + "SET email=?, login=?, name=?, birthday=? "
                        + "WHERE user_id=?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        return getUserById(user.getId());
    }

    @Override
    public User getUserById(Integer id) {
        return jdbcTemplate.queryForObject(format(""
                + "SELECT user_id, email, login, name, birthday "
                + "FROM users "
                + "WHERE user_id=%d", id), new UserMapper());
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query(""
                + "SELECT user_id, email, login, name, birthday "
                + "FROM users", new UserMapper());
    }

    @Override
    public boolean containsUser(Integer id) {
        try {
            getUserById(id);
            return true;
        } catch (EmptyResultException e) {
            return false;
        }
    }
//    private final Map<Integer, User> users = new HashMap<>();
//    private int generateId = 1;

//    @Override
//    public User addUser(User user) {
//        user.setId(generateId);
//        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
//            user.setName(user.getLogin());
//        }
//        users.put(generateId++, user);
//        return user;
//    }
//
//    @Override
//    public User updateUser(User user) {
//        if (users.containsKey(user.getId())) {
//            users.put(user.getId(), user);
//            log.debug("Обновлена информация о пользователя " + user.getName() + ", с id = " + user.getId());
//        } else if (user.getId() == null) {
//            user.setId(generateId++);
//            users.put(user.getId(), user);
//            log.debug("Добавлен новый пользователь " + user.getName());
//        } else {
//            log.debug("Не удалось обновить данные о пользователе");
//            throw new InvalidIdException("Неверный идентификатор пользователя");
//        }
//        return user;
//    }
//
//    @Override
//    public User getUserById(Integer id) {
//        User user = users.get(id);
//        if (user == null) {
//            log.debug("Пользователь с id=" + id + " не найден");
//            throw new InvalidIdException("Неверный идентификатор пользователя");
//        }
//        return user;
//    }
//
//    @Override
//    public List<User> getUsers() {
//        return new ArrayList<>(users.values());
//    }
}
