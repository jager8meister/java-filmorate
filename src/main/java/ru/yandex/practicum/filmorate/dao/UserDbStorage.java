package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.util.UserStorage;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.time.LocalDate;
import java.util.*;

@Component
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final String selectFromUsers = "SELECT * FROM users";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private void checkUserDuplicates(User user) {
        List<Map<String, Object>> res = jdbcTemplate.queryForList(selectFromUsers);
        for (Map<String, Object> elem : res) {
            if (elem.get("EMAIL").equals(user.getEmail())
            && elem.get("LOGIN").equals(user.getLogin())
            && elem.get("BIRTHDAY").toString().equals(user.getBirthday().toString())) {
                throw new StorageException("User already in the base");
            }
        }
    }

    private long getId(User user) {
        List<Map<String, Object>> res = jdbcTemplate.queryForList(selectFromUsers);
        for (Map<String, Object> elem : res) {
            if (elem.get("EMAIL").equals(user.getEmail())
                    && elem.get("LOGIN").equals(user.getLogin())
                    && elem.get("BIRTHDAY").toString().equals(user.getBirthday().toString())) {
                return Long.parseLong(elem.get("ID").toString());
            }
        }
        throw new StorageException("No such user in the base");
    }

    private void checkId(long id) {
        List<Map<String, Object>> res = jdbcTemplate.queryForList(selectFromUsers);
        for (Map<String, Object> elem : res) {
            if (elem.get("ID").toString().equals(String.valueOf(id))) {
                return ;
            }
        }
        throw new StorageException("No user with id " + id);
    }

    @Override
    public User addUser(User user) {
        if (UserValidator.valid(user)) {
            user = UserValidator.checkName(user);
            SimpleJdbcInsert insertData = new
                    SimpleJdbcInsert(jdbcTemplate).
                    withTableName("users").
                    usingColumns("email", "login",
                    "name", "birthday")
                    .usingGeneratedKeyColumns("id");
            checkUserDuplicates(user);
            insertData.executeAndReturnKey(user.toMap()).toString();
            user.setId(getId(user));
            return user;
        }
        throw new StorageException("Invalid user.");
    }

    @Override
    public User updateUser(User user) {
        if (UserValidator.valid(user)) {
            checkId(user.getId());
            String sqlQuery = "update users set " +
                    "email = ?, login = ?, name = ?, birthday = ? " +
                    "where id = ?";
            jdbcTemplate.update(sqlQuery
                    , user.getEmail()
                    , user.getLogin()
                    , user.getName()
                    , user.getBirthday()
                    , user.getId());
            return user;
        }
        throw new StorageException("Invalid user.");
    }

    @Override
    public void deleteUser(User user) {
        User check = getUserById(user.getId());
        if (check.getName().equals(user.getName()) &&
            check.getLogin().equals(user.getLogin()) &&
            check.getEmail().equals(user.getEmail()) &&
            check.getBirthday().equals(user.getBirthday())) {
            jdbcTemplate.execute("DELETE FROM users WHERE id = " + user.getId());
            jdbcTemplate.execute("DELETE FROM friends WHERE user_id = " + user.getId());
            jdbcTemplate.execute("DELETE FROM friends WHERE friend_id = " + user.getId());
        } else {
            throw new StorageException("Invalid user");
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        List<User> res = new ArrayList<>();
        List<Map<String, Object>> raw = jdbcTemplate.queryForList(selectFromUsers);
        for (Map<String, Object> elem : raw) {
            User user = getUserFromBaseElem(elem);
            res.add(user);
        }
        return res;
    }

    private User getUserFromBaseElem(Map<String, Object> elem) {
        User user = new User();
        user.setName(elem.get("NAME").toString());
        user.setBirthday(LocalDate.parse(elem.get("BIRTHDAY").toString()));
        user.setLogin(elem.get("LOGIN").toString());
        user.setEmail(elem.get("EMAIL").toString());
        user.setId(Long.parseLong(elem.get("ID").toString()));
        user.setFriendsIds(getFriendsIds(user.getId()));
        return user;
    }

    @Override
    public User getUserById(long id) {
        List<Map<String, Object>> res = jdbcTemplate.queryForList(selectFromUsers);
        for (Map<String, Object> elem : res) {
            if (elem.get("ID").toString().equals(String.valueOf(id))) {
                return getUserFromBaseElem(elem);
            }
        }
        throw new StorageException("No user with id " + id);
    }


    @Override
    public void addFriend(long id, long friendId) {
        checkId(id);
        checkId(friendId);
        SimpleJdbcInsert insertData = new
                SimpleJdbcInsert(jdbcTemplate).
                withTableName("friends").
                usingColumns("user_id", "friend_id")
                .usingGeneratedKeyColumns("id");
        Map<String, Long> params = new HashMap<>();
        params.put("user_id", id);
        params.put("friend_id", friendId);
        insertData.execute(params);
    }

    private Set<Long> getFriendsIds(long id) {
        Set<Long> ids = new HashSet<>();
        List<Map<String, Object>> res = jdbcTemplate.queryForList("SELECT friend_id FROM friends WHERE USER_ID = " + id);
        for (Map<String, Object> elem : res) {
            Long friend = Long.parseLong(elem.get("friend_id").toString());
            ids.add(friend);
        }
        return ids;
    }

    @Override
    public Collection<User> getAllFriends(long id) {
        checkId(id);
        List<Map<String, Object>> res = jdbcTemplate.queryForList("SELECT friend_id FROM friends WHERE USER_ID = " + id);
        Set<User> ids = new HashSet<>();
        for (Map<String, Object> elem : res) {
            Long friend = Long.parseLong(elem.get("friend_id").toString());
            ids.add(getUserById(friend));
        }
        return ids;
    }

    @Override
    public Collection<User> getCommonFriends(long id, long otherId) {
        Collection<Long> friendIds = getUserById(id).getFriendsIds();
        Collection<Long> otherFriendIds = getUserById(otherId).getFriendsIds();
        Set<Long> resIds = new HashSet<>();
        for (Long elemId: friendIds) {
            if (otherFriendIds.contains(elemId))
                resIds.add(elemId);
        }
        Collection<User> res = new HashSet<>();
        for (Long elemId : resIds) {
            res.add(getUserById(elemId));
        }
        return res;
    }

    @Override
    public User removeFriend(long id, long friendId) {
        checkId(id);
        checkId(friendId);
        jdbcTemplate.execute("DELETE FROM friends WHERE user_id = " + id + " AND friend_id = " + friendId);
        jdbcTemplate.execute("DELETE FROM friends WHERE user_id = " + friendId + " AND friend_id = " + id);
        return getUserById(id);
    }
}
