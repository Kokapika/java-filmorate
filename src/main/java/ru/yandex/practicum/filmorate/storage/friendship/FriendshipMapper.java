package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipMapper implements RowMapper<Friendship> {
    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        Friendship friendship = new Friendship();
        friendship.setFromUserId(rs.getInt("from_user_id"));
        friendship.setToUserId(rs.getInt("to_user_id"));
        friendship.setAgree(rs.getBoolean("isAgree"));
        return friendship;
    }
}