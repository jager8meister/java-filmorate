package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private FilmService service = new FilmService();

    @PostMapping
    public @ResponseBody ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) throws ValidationException {
        return service.addFilm(film);
    }

    @PutMapping
    public @ResponseBody ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) throws ValidationException  {
        return service.updateFilm(film);
    }

    @GetMapping
    public @ResponseBody ResponseEntity<Collection<Film>> getAllFilms() {
        return service.getAllFilms();
    }

}
