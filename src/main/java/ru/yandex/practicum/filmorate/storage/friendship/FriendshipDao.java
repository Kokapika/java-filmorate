package ru.yandex.practicum.filmorate.storage.friendship;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EmptyResultException;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendshipDao implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Friendship addFriend(Integer fromUserId, Integer toUserId, boolean isAgree) {
        jdbcTemplate.update(""
                + "INSERT INTO friendships (from_user_id, to_user_id, isAgree) "
                + "VALUES(?, ?, ?)", fromUserId, toUserId, isAgree);
        return get(fromUserId, toUserId);
    }

    @Override
    public void deleteFriend(Integer fromUserId, Integer toUserId) {
        Friendship result = Objects.requireNonNull(get(fromUserId, toUserId));
        jdbcTemplate.update(""
                + "DELETE FROM friendships "
                + "WHERE from_user_id=? "
                + "AND to_user_id=?", fromUserId, toUserId);
        if (result.isAgree()) {
            jdbcTemplate.update(""
                    + "UPDATE friendships "
                    + "SET isAgree=false "
                    + "WHERE from_user_id=? "
                    + "AND to_user_id=?", toUserId, fromUserId);
        }
    }

    @Override
    public List<Integer> getFriends(Integer toUserId) {
        return jdbcTemplate.query(format(""
                        + "SELECT from_user_id, to_user_id, isAgree "
                        + "FROM friendships "
                        + "WHERE to_user_id=%d", toUserId), new FriendshipMapper())
                .stream()
                .map(Friendship::getFromUserId)
                .collect(Collectors.toList());
    }

    @Override
    public boolean containsFriendship(Integer fromUserId, Integer toUserId) {
        try {
            get(fromUserId, toUserId);
            return true;
        } catch (EmptyResultException e) {
            return false;
        }
    }

    private Friendship get(Integer fromUserId, Integer toUserId) {
        return jdbcTemplate.queryForObject(format(""
                + "SELECT from_user_id, to_user_id, isAgree "
                + "FROM friendships "
                + "WHERE from_user_id=%d "
                + "AND to_user_id=%d", fromUserId, toUserId), new FriendshipMapper());
    }
}
