package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.util.FilmStorage;

import java.util.Collection;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addFilm(Film film) {
        filmStorage.addFilm(film);
    }

    public void updateFilm(Film film) throws ValidationException  {
        filmStorage.updateFilm(film);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film likeFilm(long id, long userId) {
        return filmStorage.likeFilm(id, userId);
    }

    public Film deleteLike(long id, long userId) {
        return filmStorage.deleteLike(id, userId);
    }

    public Collection<Film> getPopularCounted(int count) {
        return filmStorage.getPopularCounted(count);
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }
}
