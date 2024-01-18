package ru.yandex.practicum.filmorate.storage.user_event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.user_event.EventType;
import ru.yandex.practicum.filmorate.model.user_event.OperationType;
import ru.yandex.practicum.filmorate.model.user_event.UserEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
public class UserEventDbStorage implements UserEventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserEventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addEvent(EventType eventType, int userId, int entityId, OperationType operationType) {
        String query = "INSERT INTO user_event(event_type, user_id, entity_id, operation) " +
                "VALUES(?,?,?,?)";
        int insertResult = jdbcTemplate.update(query, eventType.name(), userId, entityId, operationType.name());
        if (insertResult > 0) {
            log.info("Event type:{}, operation type: {} has added for user by ID {} entity by ID {}.",
                    eventType.name(), operationType.name(), userId, entityId);
        }
    }

    @Override
    public List<UserEvent> getEventsByUserId(int userId) {
        String query = "SELECT event_id, event_type, user_id, entity_id, timestamp, operation " +
                "FROM user_event " +
                "WHERE user_id=? " +
                "ORDER BY event_id";
        log.info("Events has returned from DB for user by ID {}.", userId);
        return jdbcTemplate.query(query, this::mapToUserEvent, userId);
    }

    private UserEvent mapToUserEvent(ResultSet rs, int rowNum) throws SQLException {
        EventType eventType = EventType.valueOf(rs.getString("event_type"));
        OperationType operationType = OperationType.valueOf(rs.getString("operation"));
        return UserEvent.builder()
                .eventId(rs.getInt("event_id"))
                .eventType(eventType)
                .userId(rs.getInt("user_id"))
                .entityId(rs.getInt("entity_id"))
                .timestamp(rs.getLong("timestamp"))
                .operation(operationType)
                .build();
    }
}