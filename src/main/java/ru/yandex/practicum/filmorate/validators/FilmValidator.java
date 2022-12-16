package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {
    private static boolean checkName(Film film) {
        if (film.getName() != null
                && !film.getName().isEmpty()
                && !film.getName().isBlank()) {
            return true;
        }
        throw new ValidationException("Invalid film's name");
    }

    private static boolean checkYear(Film film) {
        LocalDate checkDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate() != null
                && film.getReleaseDate().isAfter(checkDate)) {
            return true;
        }
        throw new ValidationException("Invalid film's release date");
    }

    private static boolean checkDescriptionLength(Film film) {
        if (film.getDescription() != null
                && film.getDescription().length() <= 200) {
            return true;
        }
        throw new ValidationException("Invalid description length");
    }

    private static boolean checkDuration(Film film) {
        if (film.getDuration() != null && film.getDuration() > 0) {
            return true;
        }
        throw new ValidationException("Invalid film's duration");
    }

    public static boolean valid(Film film) {
        return (checkName(film)
                && checkDescriptionLength(film)
                && checkYear(film)
                && checkDuration(film));
    }
}
