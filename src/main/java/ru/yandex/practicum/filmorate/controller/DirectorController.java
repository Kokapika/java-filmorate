package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorDbService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorDbService directorDbService;

    @GetMapping
    public List<Director> getDirectors() {
        return directorDbService.getDirectors();
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable int id) {
        return directorDbService.getDirectorById(id);
    }

    @PostMapping
    public Director addDirector(@Valid @RequestBody Director director) {
        log.info("DirectorController addDirector directorName {}", director.getName());
        return directorDbService.addDirector(director);
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) {
        log.info("DirectorController updateDirector directorId {}", director.getId());
        return directorDbService.updateDirector(director);
    }

    @DeleteMapping("/{id}")
    public int deleteDirectorById(@PathVariable Integer id) {
        log.info("DirectorController deleteDirectorById directorId {}", id);
        return directorDbService.deleteDirectorById(id);
    }
}
