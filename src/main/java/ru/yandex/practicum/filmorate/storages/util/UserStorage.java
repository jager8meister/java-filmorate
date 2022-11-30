package ru.yandex.practicum.filmorate.storages.util;

import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    User addUser(User user) throws StorageException;
    User updateUser(User user) throws StorageException ;
    void deleteUser(User user) throws StorageException ;
}
