package ru.yandex.practicum.filmorate.storages.util;

import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User addUser(User user) throws StorageException;
    User updateUser(User user) throws StorageException ;
    void deleteUser(User user) throws StorageException ;
    Collection<User> getAllUsers();
    User getUserById(long id);
    void addFriend(long id, long friendId);
    Collection<User> getAllFriends(long id);
    Collection<User> getCommonFriends(long id, long otherId);
    User removeFriend(long id, long friendId);
}
