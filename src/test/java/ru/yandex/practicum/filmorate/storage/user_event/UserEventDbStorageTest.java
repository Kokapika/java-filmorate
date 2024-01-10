package ru.yandex.practicum.filmorate.storage.user_event;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.user_event.EventType;
import ru.yandex.practicum.filmorate.model.user_event.OperationType;
import ru.yandex.practicum.filmorate.model.user_event.UserEvent;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserEventDbStorageTest {

    @Autowired
    private UserEventStorage userEventStorage;

    @Autowired
    private UserStorage userStorage;
    private User user;

    @BeforeEach
    void setUp() {
        createVariables();
        userStorage.createUser(user);
    }

    @Test
    void addEventAndGetEventsByUserId() {
        userEventStorage.addEvent(EventType.FRIEND, user.getId(), 2, OperationType.ADD);
        userEventStorage.addEvent(EventType.LIKE, user.getId(), 3, OperationType.ADD);
        userEventStorage.addEvent(EventType.REVIEW, user.getId(), 4, OperationType.ADD);
        userEventStorage.addEvent(EventType.REVIEW, user.getId(), 4, OperationType.UPDATE);

        List<UserEvent> eventsByUserId = userEventStorage.getEventsByUserId(user.getId());

        assertThat(eventsByUserId.size()).isEqualTo(4);
        assertThat(eventsByUserId.get(0).getEventId()).isEqualTo(1);
    }

    private void createVariables() {
        user = User.builder()
                .name("Vasya")
                .email("mail@mail.ru")
                .login("Vasvas")
                .birthday(LocalDate.of(2000, 8, 6))
                .build();
    }
}