package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
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

    public @ResponseBody ResponseEntity<User> getUserById(long id) {
        try {
            User user = inMemoryUserStorage.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (StorageException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND );
        }
    }

    public @ResponseBody ResponseEntity<User> addFriend(long id, long friendId) {
        try {
            inMemoryUserStorage.addFriend(id, friendId);
            return new ResponseEntity<>(inMemoryUserStorage.getUserById(friendId), HttpStatus.OK);
        } catch (StorageException e) {
            return new ResponseEntity<>(inMemoryUserStorage.getUserById(id), HttpStatus.NOT_FOUND);
        }
    }

    public @ResponseBody ResponseEntity<Collection<User>> getAllFriends(long id) {
        try {
            return new ResponseEntity<>(inMemoryUserStorage.getAllFriends(id), HttpStatus.OK);
        } catch (StorageException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public @ResponseBody ResponseEntity<Collection<User>> getCommonFriends(long id, long otherId) {
        try {
            return new ResponseEntity<>(inMemoryUserStorage.getCommonFriends(id, otherId), HttpStatus.OK);
        } catch (StorageException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
