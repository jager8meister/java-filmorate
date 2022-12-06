package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storages.util.FilmStorage;

import java.util.Collection;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(FilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public void addFilm(Film film) {
        inMemoryFilmStorage.addFilm(film);
    }

    public void updateFilm(Film film) throws ValidationException  {
        inMemoryFilmStorage.updateFilm(film);
    }

    public Collection<Film> getAllFilms() {
        return inMemoryFilmStorage.getAllFilms();
    }

    public Film likeFilm(long id, long userId) {
        return inMemoryFilmStorage.likeFilm(id, userId);
    }

    public Film deleteLike(long id, long userId) {
        return inMemoryFilmStorage.deleteLike(id, userId);
    }

    public Collection<Film> getPopularCounted(int count) {
        return inMemoryFilmStorage.getPopularCounted(count);
    }

    public Film getFilmById(long id) {
        return inMemoryFilmStorage.getFilmById(id);
    }
}
