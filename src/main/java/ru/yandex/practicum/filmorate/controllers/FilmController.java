package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private Map<Long, Film> filmMap = new HashMap<>();

    @PostMapping
    public @ResponseBody ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (filmMap.containsKey(film.getId())) {
            return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
        } else {
            return checkAndSend(film);
        }
    }

    @PutMapping
    public @ResponseBody ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) throws ValidationException  {
        if (filmMap.containsKey(film.getId())) {
            return checkAndSend(film);
        } else {
            return new ResponseEntity<>(film, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public @ResponseBody ResponseEntity<Collection<Film>> getAllFilms() {
        return new ResponseEntity<>(filmMap.values(), HttpStatus.OK);
    }

    private ResponseEntity<Film> checkAndSend(Film film) {
        try {
            if (FilmValidator.valid(film)) {
                if (film.getId() == null) {
                    film.setId(idGenerator());
                }
                filmMap.put(film.getId(), film);
                return new ResponseEntity<>(film, HttpStatus.OK);
            }
        }
        catch (ValidationException e) {
            log.error(e.toString());
        }
        return new ResponseEntity<>(film, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private long idGenerator() {
        long id = 1;
        while (true) {
            if (filmMap.containsKey(id)) {
                id++;
            } else {
                return id;
            }
        }
    }
}
