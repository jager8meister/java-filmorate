package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private Map<Integer, User> userMap = new HashMap<>();

    @PostMapping(value = "/add")
    public @ResponseBody ResponseEntity<User> addUser(@RequestBody User user) {
        if (userMap.containsKey(user.getId())) {
            return new ResponseEntity<>(user, HttpStatus.IM_USED);
        } else {
            userMap.put(user.getId(), user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @PutMapping(value = "/update")
    public @ResponseBody ResponseEntity<User> updateUser(@RequestBody User user) {
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/all")
    public @ResponseBody ResponseEntity<Map<Integer, User>> getAll() {
        return new ResponseEntity<>(userMap, HttpStatus.OK);
    }
}
