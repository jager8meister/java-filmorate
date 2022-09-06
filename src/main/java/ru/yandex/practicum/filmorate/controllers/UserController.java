package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    private Map<Integer, User> userMap = new HashMap<>();

    private User checkName(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }

    @PostMapping(value = "/add")
    public @ResponseBody ResponseEntity<User> addUser(@RequestBody User user) throws ValidationException {
        if (userMap.containsKey(user.getId())) {
            return new ResponseEntity<>(user, HttpStatus.IM_USED);
        } else {
            if (UserValidator.valid(user)) {
                user = checkName(user);
                userMap.put(user.getId(), user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
            }
        }
    }

    @PutMapping(value = "/update")
    public @ResponseBody ResponseEntity<User> updateUser(@RequestBody User user) throws ValidationException {
        if (userMap.containsKey(user.getId())) {
            if (UserValidator.valid(user)) {
                user = checkName(user);
                userMap.put(user.getId(), user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(user, HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/all")
    public @ResponseBody ResponseEntity<Map<Integer, User>> getAllUsers() {
        return new ResponseEntity<>(userMap, HttpStatus.OK);
    }
}
