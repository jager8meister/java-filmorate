package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;

@Service
@Slf4j
public class UserService {
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    public @ResponseBody ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        try {
            inMemoryUserStorage.addUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (StorageException e) {
            log.error(e.toString());
            if (e.getMessage().equals("Invalid user.")) {
                return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
    }

    public @ResponseBody ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        try {
            inMemoryUserStorage.updateUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (StorageException e) {
            log.error(e.toString());
            if (e.getMessage().equals("Invalid user.")) {
                return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
    }

    public @ResponseBody ResponseEntity<Collection<User>> getAllUsers() {
        return new ResponseEntity<>(inMemoryUserStorage.getAllUsers(), HttpStatus.OK);
    }
}
