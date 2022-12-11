package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.util.FilmStorage;

import java.util.Collection;
import java.util.List;

@Component
@Qualifier("filmDbStorage ")
public class FilmDbStorage implements FilmStorage {

    @Override
    public Film addFilm(Film film) throws StorageException {
        return null;
    }

    @Override
    public Film updateFilm(Film film) throws StorageException {
        return null;
    }

    @Override
    public void deleteFilm(Film film) throws StorageException {

    }

    @Override
    public Film getFilmById(long id) {
        return null;
    }

    @Override
    public List<Film> getPopularCounted(int count) {
        return null;
    }

    @Override
    public Film deleteLike(long id, long userId) {
        return null;
    }

    @Override
    public Film likeFilm(long id, long userId) {
        return null;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return null;
    }
}
