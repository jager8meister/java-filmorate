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

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private void checkUserDuplicates(User user) {
        List<Map<String, Object>> res = this.jdbcTemplate.queryForList("SELECT * FROM usersBase" );
        for (Map<String, Object> elem : res ) {
            if (elem.get("EMAIL").equals(user.getEmail())
            && elem.get("LOGIN").equals(user.getLogin())
            && elem.get("BIRTHDAY").toString().equals(user.getBirthday().toString())) {
                throw new StorageException("User already in the base");
            }
        }
    }

    private long getId(User user) {
        List<Map<String, Object>> res = this.jdbcTemplate.queryForList("SELECT * FROM usersBase" );
        for (Map<String, Object> elem : res ) {
            if (elem.get("EMAIL").equals(user.getEmail())
                    && elem.get("LOGIN").equals(user.getLogin())
                    && elem.get("BIRTHDAY").toString().equals(user.getBirthday().toString())) {
                return Long.parseLong(elem.get("ID").toString());
            }
        }
        throw new StorageException("No such user in the base");
    }

    private void checkId(long id) {
        List<Map<String, Object>> res = this.jdbcTemplate.queryForList("SELECT * FROM usersBase" );
        for (Map<String, Object> elem : res ) {
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
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("email", user.getEmail());
            params.put("login", user.getLogin());
            params.put("name", user.getName());
            params.put("birthday", user.getBirthday());

            SimpleJdbcInsert insertData = new
                    SimpleJdbcInsert(jdbcTemplate).
                    withTableName("usersBase").
                    usingColumns("email", "login",
                    "name", "birthday")
                    .usingGeneratedKeyColumns("id");

            checkUserDuplicates(user);
            String res = insertData.executeAndReturnKey(user.toMap()).toString();
            user.setId(getId(user));
            return user;
        }
        throw new StorageException("Invalid user.");
    }

    @Override
    public User updateUser(User user) {
        if (UserValidator.valid(user)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", user.getId());
            params.put("email", user.getEmail());
            params.put("login", user.getLogin());
            params.put("name", user.getName());
            params.put("birthday", user.getBirthday());
            params.put("friendsIds", user.getFriendsIds());

            String sqlQuery = "update usersBase set " +
                    "email = ?, login = ?, name = ?, birthday = ? " +
                    "where id = ?";

            jdbcTemplate.update(sqlQuery
                    , user.getEmail()
                    , user.getLogin()
                    , user.getName()
                    , user.getBirthday()
                    , user.getId());
            checkId(user.getId());
            return user;
        }
        throw new StorageException("Invalid user.");
    }

    @Override
    public void deleteUser(User user) {

    }

    @Override
    public Collection<User> getAllUsers() {
        List<User> res = new ArrayList<>();
        List<Map<String, Object>> raw = this.jdbcTemplate.queryForList("SELECT * FROM usersBase" );
        for (Map<String, Object> elem : raw ) {
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
        user.setFriendsIds(getFriendsIds(elem.get("FRIENDSIDS")));
        return user;
    }

    private Set<Long> getFriendsIds(Object friends) {
        Set<Long> res = new HashSet<>();
        if (friends != null) {
            String str = friends.getClass().toString();
        }
        return res;
    }

    @Override
    public User getUserById(long id) {
        List<Map<String, Object>> res = this.jdbcTemplate.queryForList("SELECT * FROM usersBase" );
        for (Map<String, Object> elem : res ) {
            if (elem.get("ID").toString().equals(String.valueOf(id))) {
                User user = getUserFromBaseElem(elem);
                return user;
            }
        }
        throw new StorageException("No user with id " + id);
    }


    @Override
    public void addFriend(long id, long friendId) {

    }

    @Override
    public Collection<User> getAllFriends(long id) {
        return null;
    }

    @Override
    public Collection<User> getCommonFriends(long id, long otherId) {
        return null;
    }

    @Override
    public User removeFriend(long id, long friendId) {
        return null;
    }
}
