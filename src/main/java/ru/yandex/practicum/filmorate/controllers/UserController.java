package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private Map<Long, User> userMap = new HashMap<>();

    private ResponseEntity<User> checkAndSend(User user) {
        try {
            if (UserValidator.valid(user)) {
                if (user.getId() == null) {
                    user.setId(idGenerator());
                }
                user = UserValidator.checkName(user);
                userMap.put(user.getId(), user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        } catch (ValidationException e) {
            log.error(e.toString());
        }
        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public @ResponseBody ResponseEntity<User> addUser(@Valid @RequestBody User user) throws ValidationException {
        if (userMap.containsKey(user.getId())) {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        } else {
            return checkAndSend(user);
        }
    }

    @PutMapping
    public @ResponseBody ResponseEntity<User> updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (userMap.containsKey(user.getId())) {
            return checkAndSend(user);
        } else {
            return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public @ResponseBody ResponseEntity<Collection<User>> getAllUsers() {
        return new ResponseEntity<>(userMap.values(), HttpStatus.OK);
    }

    private long idGenerator() {
        long id = 1;
        while (true) {
            if (userMap.containsKey(id)) {
                id++;
            } else {
                return id;
            }
        }
    }
}
