package ru.yandex.practicum.filmorate.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerValidationTest {
    UserController userController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
    }

    @Test
    public void addUser() {
        User user = new User("Зверь", "Вася", "abc@yandex.ru", LocalDate.of(2010, 10, 12));
        userController.addUser(user);

        User getUser = userController.getUsers().get(0);

        assertNotNull(userController.getUsers());
        assertEquals(getUser.getId(), 1);
        assertEquals(getUser.getLogin(), "Зверь");
        assertEquals(getUser.getEmail(), "abc@yandex.ru");
        assertEquals(getUser.getBirthday(), LocalDate.of(2010, 10, 12));
        assertEquals(getUser.getName(), "Вася");
    }

    @Test
    public void emptyUserEmail() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            User user = new User("Зверь", "Вася", "", LocalDate.of(2010, 10, 12));
            userController.addUser(user);
        });

        assertEquals("Почта не может быть пустой", exception.getMessage());

        ValidationException e = assertThrows(ValidationException.class, () -> {
            User user = new User("Зверь", "Вася", "   ", LocalDate.of(2010, 10, 12));
            userController.addUser(user);
        });

        assertEquals("Почта не может быть пустой", e.getMessage());
    }

    @Test
    public void emailWithoutDogSymbol() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            User user = new User("Зверь", "Вася", "abrakatabra", LocalDate.of(2010, 10, 12));
            userController.addUser(user);
        });

        assertEquals("Почта должна содержать символ '@'", exception.getMessage());
    }

    @Test
    public void emptyLoginUser() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            User user = new User("", "Вася", "abc@yandex.ru", LocalDate.of(2010, 10, 12));
            userController.addUser(user);
        });

        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());

        ValidationException e = assertThrows(ValidationException.class, () -> {
            User user = new User("   ", "Вася", "abc@yandex.ru", LocalDate.of(2010, 10, 12));
            userController.addUser(user);
        });

        assertEquals("Логин не может быть пустым и содержать пробелы", e.getMessage());
    }

    @Test
    public void emptyNameUser() {
        User user = new User("Вася", "", "abc@yandex.ru", LocalDate.of(2010, 10, 12));
        userController.addUser(user);

        assertEquals(user.getName(), "Вася");

        User u = new User("Вася", "    ", "abc@yandex.ru", LocalDate.of(2010, 10, 12));
        userController.addUser(u);

        assertEquals(u.getName(), "Вася");
    }

    @Test
    public void birthdayInFuture() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            User user = new User("Зверь", "Вася", "abc@yandex.ru", LocalDate.of(2030, 10, 12));
            userController.addUser(user);
        });

        assertEquals("Дата рождения не может быть в будушем", exception.getMessage());
    }
}
