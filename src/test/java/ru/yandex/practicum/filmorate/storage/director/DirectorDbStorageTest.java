package ru.yandex.practicum.filmorate.storage.director;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DirectorDbStorageTest {
    private final DirectorStorage directorStorage;

    @Test
    void createDirector() {
        Director director = directorStorage.createDirector(new Director("DirectorName"));

        Director directorById = directorStorage.getDirectorById(director.getId());

        assertEquals(director, directorById);
    }

    @Test
    void createTwoDirectorName() {
        Director director = directorStorage.createDirector(new Director("DirectorName"));
        Director director2 = directorStorage.createDirector(new Director("DirectorName2"));
        List<Director> directorList = List.of(director, director2);

        List<Director> allDirectors = directorStorage.getAllDirectors();

        assertEquals(directorList, allDirectors);
    }

    @Test
    void updateDirector() {
        Director director = directorStorage.createDirector(new Director("DirectorName"));
        Director updateDirector = new Director("DirectorName2");
        updateDirector.setId(director.getId());

        Director returnDirector = directorStorage.updateDirector(updateDirector);

        assertEquals(updateDirector, returnDirector);
    }

    @Test
    void deleteDirectorById() {
        Director director = directorStorage.createDirector(new Director("DirectorName"));

        directorStorage.deleteDirectorById(director.getId());

        assertThatThrownBy(() -> directorStorage.getDirectorById(director.getId()))
                .isInstanceOf(NotFoundException.class);
    }
}