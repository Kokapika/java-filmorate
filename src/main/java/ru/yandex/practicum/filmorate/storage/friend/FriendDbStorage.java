package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Status;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    public void addFriend(Integer id, Integer friendId, Status status) {
        String sql = "INSERT INTO friends (user_id, friend_id, status_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, id, friendId, status.getStatusId());
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
    }

    @Override
    public HashMap<Integer, Status> getFriends(Integer id) {
        String sql = "SELECT f.friend_id, s.name " +
                "FROM friends f " +
                "LEFT JOIN status s ON f.status_id = s.status_id" +
                " WHERE f.user_id = ?";
        HashMap<Integer, Status> userFriends = new HashMap<>();

        jdbcTemplate.query(sql, (rs, rowNum) -> {
            userFriends.put(rs.getInt("friend_id"), Status.valueOf(rs.getString("name")));
            return null;
        }, id);
        return userFriends;
    }

    @Override
    public void updateStatus(Integer id, Integer friendId, Status status) {
        String sql = "UPDATE friends SET status_id = ? WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, status.getStatusId(), id, friendId);
    }
}
