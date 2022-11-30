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
}
