package ru.yandex.practicum.filmorate.storages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.utils.FilmComparator;
import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.util.FilmStorage;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> filmMap = new HashMap<>();

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
            throw e;
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

    public Film likeFilm(long id, long userId) {
        if (filmMap.containsKey(id)) {
            if (userId <= 0) {
                throw new StorageException("Non positive user id");
            }
            filmMap.get(id).getLikes().add(userId);
            return filmMap.get(id);
        } else {
            throw new StorageException("Invalid film id.");
        }
    }

    public Film deleteLike(long id, long userId) {
        if (filmMap.containsKey(id)) {
            if (filmMap.get(id).getLikes().contains(userId)) {
                filmMap.get(id).getLikes().remove(userId);
                return filmMap.get(id);
            } else {
                throw new StorageException("Delete like: Invalid user id.");
            }
        } else {
            throw new StorageException("Delete like: Invalid film id.");
        }
    }

    public List<Film> getPopularCounted(int count) {
        List<Film> all = new ArrayList<>(filmMap.values());
        all.sort(new FilmComparator());
        List<Film> res;
        if (count < filmMap.size()) {
            res = all.subList(0, count);
        } else {
            res = all.subList(0, filmMap.size());
        }
        return res;
    }

    public Film getFilmById(long id) {
        if (filmMap.containsKey(id)) {
            return filmMap.get(id);
        } else {
            throw new StorageException("Invalid film id.");
        }
    }
}
