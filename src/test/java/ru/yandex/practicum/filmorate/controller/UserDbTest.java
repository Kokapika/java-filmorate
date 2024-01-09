package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDbTest {

    private final UserStorage userStorage;

    @Test
    void createUser() {
        User testUser = new User("abububugaga@mail.ru", "Koka", "Kokapika",
                LocalDate.of(1999, 1, 1));

        userStorage.createUser(testUser);

        assertEquals("Koka", testUser.getLogin());
        assertEquals(1, testUser.getId());
        assertEquals(1, userStorage.getAllUsers().size());
    }

    @Test
    void updateUser() {
        User testUser = new User("abububugaga@mail.ru", "Koka", "Kokapika",
                LocalDate.of(1999, 1, 1));

        userStorage.createUser(testUser);

        User updateUser = new User(1, "abuga@mail.ru", "Kapika", "Kokapika",
                LocalDate.of(1999, 1, 1));

        userStorage.updateUser(updateUser);

        assertEquals("Kapika", updateUser.getLogin());
    }

    @Test
    void deleteUser() {
        User testUser = new User("abububugaga@mail.ru", "Koka", "Kokapika",
                LocalDate.of(1999, 1, 1));

        userStorage.createUser(testUser);

        assertEquals(1, userStorage.getAllUsers().size());

        userStorage.deleteUserById(1);

        assertEquals(0, userStorage.getAllUsers().size());
    }

    @Test
    void findAllUsers() {
        User testUser = new User("abububugaga@mail.ru", "Koka", "Kokapika",
                LocalDate.of(1999, 1, 1));

        userStorage.createUser(testUser);

        User testUser2 = new User("abuga@mail.ru", "Kapika", "Kokapika",
                LocalDate.of(1999, 1, 1));

        userStorage.createUser(testUser2);

        assertEquals(2, userStorage.getAllUsers().size());
    }

    @Test
    void getUserById() {
        User testUser = new User("abububugaga@mail.ru", "Koka", "Kokapika",
                LocalDate.of(1999, 1, 1));

        userStorage.createUser(testUser);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void getFriends() {
        User testUser = new User("abububugaga@mail.ru", "Koka", "Kokapika",
                LocalDate.of(1999, 1, 1));
        userStorage.createUser(testUser);
        User testUser2 = new User("abuga@mail.ru", "Kapika", "Kokapika",
                LocalDate.of(2000, 1, 1));
        userStorage.createUser(testUser2);
        User testUser3 = new User("gaga@mail.ru", "Pika", "Kokapi",
                LocalDate.of(2001, 1, 1));
        userStorage.createUser(testUser3);

        userStorage.addFriend(1, 3);
        userStorage.addFriend(1, 2);
        userStorage.addFriend(2, 1);
        userStorage.addFriend(2, 3);
        userStorage.addFriend(3, 2);
        userStorage.addFriend(3, 1);

        assertEquals(2, userStorage.getFriendsById(1).size());
    }

    @Test
    void deleteFriend() {
        User testUser = new User("abububugaga@mail.ru", "Koka", "Kokapika",
                LocalDate.of(1999, 1, 1));
        userStorage.createUser(testUser);
        User testUser2 = new User("abuga@mail.ru", "Kapika", "Kokapika",
                LocalDate.of(2000, 1, 1));
        userStorage.createUser(testUser2);
        User testUser3 = new User("gaga@mail.ru", "Pika", "Kokapi",
                LocalDate.of(2001, 1, 1));
        userStorage.createUser(testUser3);

        userStorage.addFriend(1, 3);
        userStorage.addFriend(1, 2);
        userStorage.addFriend(2, 1);
        userStorage.addFriend(2, 3);
        userStorage.addFriend(3, 2);
        userStorage.addFriend(3, 1);

        userStorage.deleteFriend(1, 3);
        assertEquals(1, userStorage.getFriendsById(1).size());
    }

    @Test
    void getCommonFriends() {
        User testUser = new User("abububugaga@mail.ru", "Koka", "Kokapika",
                LocalDate.of(1999, 1, 1));
        userStorage.createUser(testUser);
        User testUser2 = new User("abuga@mail.ru", "Kapika", "Kokapika",
                LocalDate.of(2000, 1, 1));
        userStorage.createUser(testUser2);
        User testUser3 = new User("gaga@mail.ru", "Pika", "Kokapi",
                LocalDate.of(2001, 1, 1));
        userStorage.createUser(testUser3);

        userStorage.addFriend(1, 3);
        userStorage.addFriend(1, 2);
        userStorage.addFriend(2, 1);
        userStorage.addFriend(2, 3);
        userStorage.addFriend(3, 2);
        userStorage.addFriend(3, 1);

        assertEquals(1, userStorage.getCommonFriends(1, 3).size());
        assertEquals(2, userStorage.getCommonFriends(1, 3).get(0).getId());
    }
}