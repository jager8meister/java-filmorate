package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;

import java.util.Collection;

@Service
@Slf4j
public class UserService {

    InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addUser(User user) {
        try {
            inMemoryUserStorage.addUser(user);
            return;
        } catch (StorageException e) {
            log.error(e.toString());
        }
        throw new StorageException("Bad request");
    }

    public void updateUser(User user) {
        try {
            inMemoryUserStorage.updateUser(user);
            return;
        } catch (StorageException e) {
            log.error(e.toString());
            if (e.getMessage().equals("Invalid user.")) {
                throw e;
            }
        }
        throw new StorageException("Not found");
    }

    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public User getUserById(long id) {
        try {
            return inMemoryUserStorage.getUserById(id);
        } catch (StorageException e) {
            log.error(e.toString());
            throw e;
        }
    }

    public User addFriend(long id, long friendId) {
        try {
            inMemoryUserStorage.addFriend(id, friendId);
            return inMemoryUserStorage.getUserById(friendId);
        } catch (StorageException e) {
            log.error(e.toString());
            throw e;
        }
    }

    public Collection<User> getAllFriends(long id) {
        try {
            return inMemoryUserStorage.getAllFriends(id);
        } catch (StorageException e) {
            log.error(e.toString());
            throw e;
        }
    }

    public Collection<User> getCommonFriends(long id, long otherId) {
        try {
            return inMemoryUserStorage.getCommonFriends(id, otherId);
        } catch (StorageException e) {
            log.error(e.toString());
            throw e;
        }
    }

    public User removeFriend(long id, long friendId) {
        try {
            return inMemoryUserStorage.removeFriend(id, friendId);
        } catch (StorageException e) {
            log.error(e.toString());
            throw e;
        }
    }
}
