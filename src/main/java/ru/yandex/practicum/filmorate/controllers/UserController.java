package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private UserService service = new UserService();


    @PostMapping
    public @ResponseBody ResponseEntity<User> addUser(@Valid @RequestBody User user) throws ValidationException {
        return service.addUser(user);
    }

    @PutMapping
    public @ResponseBody ResponseEntity<User> updateUser(@Valid @RequestBody User user) throws ValidationException {
        return service.updateUser(user);
    }

    @GetMapping
    public @ResponseBody ResponseEntity<Collection<User>> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<User> getUserById(@PathVariable long id) {
        return service.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public @ResponseBody ResponseEntity<User> addFriend(@PathVariable long id, @PathVariable long friendId) {
        return service.addFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public @ResponseBody ResponseEntity<Collection<User>> getAllFriends(@PathVariable long id) {
        return service.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public @ResponseBody ResponseEntity<Collection<User>> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return service.getCommonFriends(id, otherId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public @ResponseBody ResponseEntity<User> removeFriend(@PathVariable long id, @PathVariable long friendId) {
        return service.removeFriend(id, friendId);
    }
}
