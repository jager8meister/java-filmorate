package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {
    static private boolean checkName(Film film) {
        if (film.getName() != null
                && !film.getName().isEmpty()
                && !film.getName().isBlank())
            return true;
        throw new ValidationException("Invalid film's name");
    }

    static private boolean checkYear(Film film) {
        LocalDate checkDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isAfter(checkDate))
            return true;
        throw new ValidationException("Invalid film's release date");
    }

    static private boolean checkDescriptionLength(Film film) {
        if (film.getDescription().length() <= 200)
            return true;
        throw new ValidationException("Invalid description length");
    }

    static private boolean checkDuration(Film film) {
        if (film.getDuration().isPositive())
            return true;
        throw new ValidationException("Invalid film's duration");
    }

    static public boolean valid(Film film) {
        return (checkName(film)
                && checkDescriptionLength(film)
                && checkYear(film)
                && checkDuration(film));
    }
}
