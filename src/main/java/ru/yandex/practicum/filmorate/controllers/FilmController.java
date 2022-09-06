package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {

    private Map<Integer, Film> filmMap = new HashMap<>();

    @PostMapping(value = "/add")
    public @ResponseBody ResponseEntity<Film> addFilm(@RequestBody Film film) {
        if (filmMap.containsKey(film.getId())) {
            return new ResponseEntity<>(film, HttpStatus.IM_USED);
        } else {
            if (FilmValidator.valid(film)) {
                filmMap.put(film.getId(), film);
                return new ResponseEntity<>(film, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(film, HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    @PutMapping(value = "/update")
    public @ResponseBody ResponseEntity<Film> updateFilm(@RequestBody Film film) {
        if (filmMap.containsKey(film.getId())) {
            if (FilmValidator.valid(film)) {
                filmMap.put(film.getId(), film);
                return new ResponseEntity<>(film, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(film, HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            return new ResponseEntity<>(film, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/all")
    public @ResponseBody ResponseEntity<Map<Integer, Film>> getAll() {
        return new ResponseEntity<>(filmMap, HttpStatus.OK);
    }
}