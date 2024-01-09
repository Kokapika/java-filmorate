package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbTest {

    private final MpaStorage mpaStorage;

    @Test
    void getById() {
        assertEquals("G", mpaStorage.getMpaById(1).getName());
        assertEquals("NC-17", mpaStorage.getMpaById(5).getName());
    }

    @Test
    void getAllMpas() {
        assertEquals(5, mpaStorage.getAllMpa().size());
    }
}