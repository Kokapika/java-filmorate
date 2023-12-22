package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public int addFriend(int id, int friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public int deleteFriend(int id, int friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.update(sql, id, friendId);
    }


    @Override
    public User createUser(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return user;
    }

    @Override
    public User updateUser(User user) throws NotFoundException {
        final String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        int result = jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId());
        if (result != 1) {
            throw new NotFoundException("Пользователь не найден");
        }
        return user;
    }

    @Override
    public void deleteAllUsers() {
        String sql = "DELETE FROM users";
        jdbcTemplate.update(sql);
    }

    @Override
    public List<User> getAllUsers() {
        final String sql = "SELECT * FROM users";
        final List<User> users = jdbcTemplate.query(sql, this::userMapper);
        if (users.isEmpty()) {
            return Collections.emptyList();
        }
        return users;
    }

    @Override
    public int deleteUserById(int id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public User getUserById(int id) throws NotFoundException {
        final String sql = "SELECT * FROM users WHERE user_id = ?";
        final List<User> users = jdbcTemplate.query(sql, this::userMapper, id);
        if (users.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        return users.get(0);
    }

    @Override
    public List<User> getFriendsById(int id) {
        String sql = "SELECT * " +
                "FROM users AS u, friends AS f " +
                "WHERE u.user_id = f.friend_id " +
                "AND f.user_id = ?";
        return jdbcTemplate.query(sql, this::userMapper, id);
    }

    @Override
    public List<User> getCommonFriends(int id, int friendId) {
        String sql = "SELECT * " +
                "FROM users AS u, friends AS f, " +
                "friends AS of WHERE u.user_id = f.friend_id " +
                "AND u.user_id = of.friend_id " +
                "AND f.user_id = ? AND of.user_id = ?";
        return jdbcTemplate.query(sql, this::userMapper, id, friendId);
    }

    private User userMapper(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }
}
