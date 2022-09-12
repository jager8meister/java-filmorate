package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private Map<String, Film> filmMap = new HashMap<>();

    @PostMapping()
    public @ResponseBody ResponseEntity<Film> addFilm(@RequestBody Film film) throws ValidationException {
        if (filmMap.containsKey(film.getName())) {
            return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
        } else {
            return checkAndSend(film);
        }
    }

    @PutMapping()
    public @ResponseBody ResponseEntity<Film> updateFilm(@RequestBody Film film) throws ValidationException  {
        if (filmMap.containsKey(film.getName())) {
            return checkAndSend(film);
        } else {
            return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public @ResponseBody ResponseEntity<Map<String, Film>> getAllFilms() {
        return new ResponseEntity<>(filmMap, HttpStatus.OK);
    }

    private ResponseEntity<Film> checkAndSend(Film film) {
        try {
            if (FilmValidator.valid(film)) {
                filmMap.put(film.getName(), film);
                return new ResponseEntity<>(film, HttpStatus.OK);
            }
        }
        catch (ValidationException e) {
            log.error(e.toString());
        }
        return new ResponseEntity<>(film, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
