package ru.yandex.practicum.filmorate.storages.util;

import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    Film addFilm(Film film) throws StorageException;
    Film updateFilm(Film film) throws StorageException ;
    void deleteFilm(Film film) throws StorageException ;
}
