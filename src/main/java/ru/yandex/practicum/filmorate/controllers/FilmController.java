package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FilmController {

    private Map<Integer, Film> filmMap = new HashMap<>();

    @PostMapping(value = "/add")
    public Film addFilm(@RequestBody Film film) {
        filmMap.put(film.getId(), film);
        return film;
    }

    @PutMapping(value = "/update")
    public Film updateFilm(@RequestBody Film film) {
        filmMap.put(film.getId(), film);
        return film;
    }

    @GetMapping(value = "/all")
    public Map<Integer, Film> getAll() {
        return filmMap;
    }
}
