package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.InMemoryFilmStorage;

import java.util.Collection;

@Service
@Slf4j
public class FilmService {

    private InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public HttpStatus addFilm(Film film) {
        try {
            inMemoryFilmStorage.addFilm(film);
            return  HttpStatus.OK;
        } catch (StorageException e) {
            log.error(e.toString());
            if (e.getMessage().equals("Invalid film.")) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return HttpStatus.BAD_REQUEST;
    }

    public HttpStatus updateFilm(Film film) throws ValidationException  {
        try {
            inMemoryFilmStorage.updateFilm(film);
            return HttpStatus.OK;
        } catch (StorageException e) {
            log.error(e.toString());
            if (e.getMessage().equals("Invalid film.")) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
        return HttpStatus.NOT_FOUND;
    }

    public Collection<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public Film likeFilm(long id, long userId) {
        try {
            return inMemoryFilmStorage.likeFilm(id, userId);
        } catch (StorageException e) {
            log.error(e.toString());
            throw e;
        }
    }

    public Film deleteLike(long id, long userId) {
        try {
            return inMemoryFilmStorage.deleteLike(id, userId);
        } catch (StorageException e) {
            log.error(e.toString());
            throw e;
        }
    }

    public Collection<Film> getPopularCounted(int count) {
        return inMemoryFilmStorage.getPopularCounted(count);
    }

    public Film getFilmById(long id) {
        try {
            return inMemoryFilmStorage.getFilmById(id);
        } catch (StorageException e) {
            log.error(e.toString());
            throw e;
        }
    }
}
