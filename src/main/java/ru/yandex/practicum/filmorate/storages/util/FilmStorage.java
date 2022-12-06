package ru.yandex.practicum.filmorate.storages.util;

import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film) throws StorageException;
    Film updateFilm(Film film) throws StorageException ;
    void deleteFilm(Film film) throws StorageException ;
    Film getFilmById(long id);
    List<Film> getPopularCounted(int count);
    Film deleteLike(long id, long userId);
    Film likeFilm(long id, long userId);
    Collection<Film> getAllFilms();
}
