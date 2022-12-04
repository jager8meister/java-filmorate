package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public @ResponseBody ResponseEntity<User> addUser(@Valid @RequestBody User user) throws ValidationException {
        try {
            service.addUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public @ResponseBody ResponseEntity<User> updateUser(@Valid @RequestBody User user) throws ValidationException {
        try {
            service.updateUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            if (e.getMessage().equals("Invalid user.")) {
                return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
            }
        }
    }

    @GetMapping
    public @ResponseBody ResponseEntity<Collection<User>> getAllUsers() {
        return new ResponseEntity<>(service.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<User> getUserById(@PathVariable long id) {
            return new ResponseEntity<>(service.getUserById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public @ResponseBody ResponseEntity<User> addFriend(@PathVariable long id, @PathVariable long friendId) {
        try {
            return new ResponseEntity<>(service.addFriend(id, friendId), HttpStatus.OK);
        } catch (StorageException e) {
            return new ResponseEntity<>(service.getUserById(id), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}/friends")
    public @ResponseBody ResponseEntity<Collection<User>> getAllFriends(@PathVariable long id) {
        return new ResponseEntity<>(service.getAllFriends(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public @ResponseBody ResponseEntity<Collection<User>> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return new ResponseEntity<>(service.getCommonFriends(id, otherId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public @ResponseBody ResponseEntity<User> removeFriend(@PathVariable long id, @PathVariable long friendId) {
        return new ResponseEntity<>(service.removeFriend(id, friendId), HttpStatus.OK);
    }
}
