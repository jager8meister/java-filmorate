package ru.yandex.practicum.filmorate.storages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.util.UserStorage;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Long, User> userMap = new HashMap<>();

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

    private User checkAndSend(User user) throws StorageException {
        try {
            if (UserValidator.valid(user)) {
                if (user.getId() == null) {
                    user.setId(idGenerator());
                }
                user = UserValidator.checkName(user);
                userMap.put(user.getId(), user);
                return user;
            }
        } catch (ValidationException e) {
            log.error(e.toString());
        }
        throw new StorageException("Invalid user.");
    }

    @Override
    public User addUser(User user) throws StorageException {
        if (userMap.containsKey(user.getId())) {
            throw new StorageException("User already exists.");
        } else {
            return checkAndSend(user);
        }
    }

    @Override
    public User updateUser(User user) throws StorageException {
        if (userMap.containsKey(user.getId())) {
            return checkAndSend(user);
        } else {
            throw new StorageException("User doesn't exist in the user base.");
        }
    }

    @Override
    public void deleteUser(User user) throws StorageException {
        if (userMap.containsKey(user.getId()) && userMap.get(user.getId()).equals(user)) {
            userMap.remove(user.getId());
        } else {
            throw new StorageException("User doesn't exist in user base.");
        }
    }

    public Collection<User> getAllUsers() {
        return userMap.values();
    }

    public User getUserById(long id) {
        if (userMap.containsKey(id)) {
            return userMap.get(id);
        } else {
            throw new StorageException("No user with id " + id);
        }
    }
}
