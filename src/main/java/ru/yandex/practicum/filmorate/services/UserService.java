package ru.yandex.practicum.filmorate.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storages.util.UserStorage;

import java.util.Collection;

@Service
@Slf4j
public class UserService {

    private UserStorage inMemoryUserStorage;

    @Autowired
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addUser(User user) {
        inMemoryUserStorage.addUser(user);
    }

    public void updateUser(User user) {
            inMemoryUserStorage.updateUser(user);
    }

    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.getAllUsers();
    }

    public User getUserById(long id) {
        return inMemoryUserStorage.getUserById(id);
    }

    public User addFriend(long id, long friendId) {
        inMemoryUserStorage.addFriend(id, friendId);
        return inMemoryUserStorage.getUserById(friendId);
    }

    public Collection<User> getAllFriends(long id) {
        return inMemoryUserStorage.getAllFriends(id);
    }

    public Collection<User> getCommonFriends(long id, long otherId) {
        return inMemoryUserStorage.getCommonFriends(id, otherId);
    }

    public User removeFriend(long id, long friendId) {
        return inMemoryUserStorage.removeFriend(id, friendId);
    }
}
