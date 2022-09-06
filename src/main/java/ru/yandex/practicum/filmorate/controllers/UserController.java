package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private Map<Integer, User> userMap = new HashMap<>();

    @PostMapping(value = "/add")
    public User addUser(@RequestBody User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    @PutMapping(value = "/update")
    public User updateUser(@RequestBody User user) {
        userMap.put(user.getId(), user);
        return user;
    }

    @GetMapping(value = "/all")
    public Map<Integer, User> getAll() {
        return userMap;
    }
}
