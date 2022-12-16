package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {

    private static boolean checkEmail(User user) {
        if (user.getEmail() != null
                && !user.getEmail().isEmpty()
                && !user.getEmail().isBlank()
                && user.getEmail().contains("@")) {
            return true;
        }
        throw new ValidationException("Invalid user email");
    }

    private static boolean checkLogin(User user) {
        if (user.getLogin() != null
                && !user.getLogin().isEmpty()
                && !user.getLogin().isBlank()
                && !user.getLogin().contains(" ")) {
            return true;
        }
        throw new ValidationException("Invalid user login");
    }

    private static boolean checkBirthday(User user) {
        if (user.getBirthday() != null
                && user.getBirthday().isBefore(LocalDate.now())) {
            return true;
        }
        throw new ValidationException("Invalid user's date of birth");
    }

    public static boolean valid(User user) throws ValidationException {
        return checkEmail(user)
                && checkLogin(user)
                && checkBirthday(user);
    }

    public static User checkName(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
