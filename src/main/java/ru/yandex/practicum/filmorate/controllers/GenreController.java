package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.services.GenreService;

import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenreController {

    private final GenreService service;

    @Autowired
    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<Genre> getGenreById(@PathVariable int id) {
        try {
            return new ResponseEntity<>(service.getGenreById(id), HttpStatus.OK);
        } catch (ValidationException e) {
            log.error("Invalid genre id");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public @ResponseBody ResponseEntity<Collection<Genre>> getAllGenres() {
        return new ResponseEntity<>(service.getAllGenres(), HttpStatus.OK);
    }
}
