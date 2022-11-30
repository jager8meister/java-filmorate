package ru.yandex.practicum.filmorate.storages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.util.FilmStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> filmMap = new HashMap<>();

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

    private Film checkAndSend(Film film) throws StorageException {
        try {
            if (FilmValidator.valid(film)) {
                if (film.getId() == null) {
                    film.setId(idGenerator());
                }
                filmMap.put(film.getId(), film);
                return film;
            }
        }
        catch (ValidationException e) {
            log.error(e.toString());
        }
        throw new StorageException("Invalid film.");
    }

    @Override
    public Film addFilm(Film film) throws StorageException {
        if (filmMap.containsKey(film.getId())) {
            throw new StorageException("Film already exists.");
        } else {
            return checkAndSend(film);
        }
    }

    @Override
    public Film updateFilm(Film film) throws StorageException {
        if (filmMap.containsKey(film.getId())) {
            return checkAndSend(film);
        } else {
            throw new StorageException("Film doesn't exist in the film base.");
        }
    }

    @Override
    public void deleteFilm(Film film) throws StorageException {
        if (filmMap.containsKey(film.getId())) {
            filmMap.remove(film.getId());
        } else {
            throw new StorageException("Film doesn't exist in film base.");
        }
    }

    public Collection<Film> getAllFilms() {
        return filmMap.values();
    }
}
