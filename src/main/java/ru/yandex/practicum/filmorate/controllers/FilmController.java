package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @PostMapping
    public @ResponseBody ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) throws ValidationException {
        return new ResponseEntity<>(film, service.addFilm(film));
    }

    @PutMapping
    public @ResponseBody ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) throws ValidationException  {
        return new ResponseEntity<>(film, service.updateFilm(film));
    }

    @GetMapping
    public @ResponseBody ResponseEntity<Collection<Film>> getAllFilms() {
        return new ResponseEntity<>(service.getAllFilms(), HttpStatus.OK);
    }

    @PutMapping("/{id}/like/{userId}")
    public @ResponseBody ResponseEntity<Film> likeFilm(@PathVariable long id, @PathVariable long userId) {
        try {
            return new ResponseEntity<>(service.likeFilm(id, userId), HttpStatus.OK);
        } catch (StorageException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public @ResponseBody ResponseEntity<Film> deleteLike(@PathVariable long id, @PathVariable long userId) {
        try {
            return new ResponseEntity<>(service.deleteLike(id, userId), HttpStatus.OK);
        } catch (StorageException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/popular")
    public @ResponseBody ResponseEntity<Collection<Film>> getPopularCounted(@RequestParam(defaultValue = "10") int count) {
        return new ResponseEntity<>(service.getPopularCounted(count), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<Film> getFilmById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(service.getFilmById(id), HttpStatus.OK);
        } catch (StorageException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
