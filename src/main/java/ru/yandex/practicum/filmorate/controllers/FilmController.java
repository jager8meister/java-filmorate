package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @PostMapping
    public @ResponseBody ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) throws ValidationException {
        try {
            service.addFilm(film);
            return new ResponseEntity<>(film, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public @ResponseBody ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) throws ValidationException  {
        try {
            service.updateFilm(film);
            return new ResponseEntity<>(film, HttpStatus.OK);
        } catch (Exception e) {
            if (e.getMessage().equals("Invalid film.")) {
                return new ResponseEntity<>(film, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                return new ResponseEntity<>(film, HttpStatus.NOT_FOUND);
            }
        }
    }

    @GetMapping
    public @ResponseBody ResponseEntity<Collection<Film>> getAllFilms() {
        return new ResponseEntity<>(service.getAllFilms(), HttpStatus.OK);
    }

    @PutMapping("/{id}/like/{userId}")
    public @ResponseBody ResponseEntity<Film> likeFilm(@PathVariable long id, @PathVariable long userId) {
        return new ResponseEntity<>(service.likeFilm(id, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public @ResponseBody ResponseEntity<Film> deleteLike(@PathVariable long id, @PathVariable long userId) {
        return new ResponseEntity<>(service.deleteLike(id, userId), HttpStatus.OK);
    }

    @GetMapping("/popular")
    public @ResponseBody ResponseEntity<Collection<Film>> getPopularCounted(@RequestParam(defaultValue = "10") int count) {
        return new ResponseEntity<>(service.getPopularCounted(count), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<Film> getFilmById(@PathVariable long id) {
        return new ResponseEntity<>(service.getFilmById(id), HttpStatus.OK);
    }

}
