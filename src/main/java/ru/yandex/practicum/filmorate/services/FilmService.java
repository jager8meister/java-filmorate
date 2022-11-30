package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class FilmService {

    private InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    public @ResponseBody ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        try {
            inMemoryFilmStorage.addFilm(film);
            return new ResponseEntity<>(film, HttpStatus.OK);
        } catch (StorageException e) {
            log.error(e.toString());
            if (e.getMessage().equals("Invalid film.")) {
                return new ResponseEntity<>(film, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
    }

    public @ResponseBody ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) throws ValidationException  {
        try {
            inMemoryFilmStorage.updateFilm(film);
            return new ResponseEntity<>(film, HttpStatus.OK);
        } catch (StorageException e) {
            log.error(e.toString());
            if (e.getMessage().equals("Invalid film.")) {
                return new ResponseEntity<>(film, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(film, HttpStatus.NOT_FOUND);
    }

    public @ResponseBody ResponseEntity<Collection<Film>> getAllFilms() {
        return new ResponseEntity<>(inMemoryFilmStorage.getAllFilms(), HttpStatus.OK);
    }
}
