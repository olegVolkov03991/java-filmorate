package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userStorage.UserStorage;
import ru.yandex.practicum.filmorate.validations.UserValidator;

import javax.validation.Valid;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserValidator userValidator;

    public UserDbStorage(JdbcTemplate jdbcTemplate, UserValidator userValidator) {
        this.jdbcTemplate = jdbcTemplate;
        this.userValidator = userValidator;
    }

    @Override
    public void createUser(User user) {
        String sqlCreateUser = "INSERT INTO USER (NAME, EMAIL, LOGIN, BIRTHDAY)" + "VALUES (?,?,?,?)";
        userValidator.validate(user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement prst = connection.prepareStatement(sqlCreateUser, new String[]{"user_id"});
            prst.setString(1, user.getName());
            prst.setString(2, user.getEmail());
            prst.setString(3, user.getLogin());
            prst.setDate(4, Date.valueOf(user.getBirthday()));
            return prst;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public void updateUser(@Valid User user) {
        String sqlUpdateUser = "UPDATE USER SET NAME=?, EMAIL=?, LOGIN=?, BIRTHDAY=?";
        userValidator.validate(user);
        if (user.getId() < 0) {
            throw new UserNotFoundException("negative id");
        }
        jdbcTemplate.update(sqlUpdateUser, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday());
    }

    @Override
    public List<User> getAllUsers() {
        String sqlGetAllUsers = "SELECT * FROM USER";
        return jdbcTemplate.query(sqlGetAllUsers, this::mapRowToUser);
    }

    @Override
    public User getUserById(int id) {
        String sqlgetUserBuId = "SELECT * FROM USER WHERE USER_ID=?";
        if (id < 0) {
            throw new UserNotFoundException("negative id");
        }
        return jdbcTemplate.queryForObject(sqlgetUserBuId, this::mapRowToUser, id);
    }

    @Override
    public void userAddFriend(int useId, int friendId) {
        String sqlStatus1 = "SELECT STATUS FROM FRIENDSHIP WHERE USER1_ID=? AND USER1_ID=?";
        String sqlStatus2 = "SELECT STATUS FROM FRIENDSHIP WHERE USER1_ID=? AND USER1_ID=?";
        String sqlUpdateFriendShip = "UPDATE FRIENDSHIP SET " + "STATUS = ? " + "WHERE ID = ?";
        String sqlInsertFriendShip = "INSERT INTO FRIENDSHIP (USER1_ID, USER2_ID, STATUS) " + "VALUES (?, ?, ?)";
        if (friendId < 0) {
            throw new UserNotFoundException("negative_id");
        }
        if (useId < 0) {
            throw new UserNotFoundException("negative id");
        }
        SqlRowSet status1 = jdbcTemplate.queryForRowSet(sqlStatus1, useId, friendId);
        SqlRowSet status2 = jdbcTemplate.queryForRowSet(sqlStatus2, friendId, useId);
        if (status1.toString().equals("Confirmed")) {
        } else if (status1.toString().equals("Not Confirmed")) {
        } else if (status2.toString().equals("Confirmed")) {
        } else if (status2.toString().equals("Not Confirmed")) {
            jdbcTemplate.update(sqlUpdateFriendShip, "Confirmed", useId);
        } else {
            jdbcTemplate.update(sqlInsertFriendShip, useId, friendId, "Not Confirmed");
        }
    }

    @Override
    public List<User> userGetFriend(int userId) {
        String sqlUserGetFriend = "SELECT U.* FROM FRIENDSHIP FS LEFT JOIN USER U ON FS.USER2_ID = U.USER_ID WHERE FS.USER1_ID = ?";
        return jdbcTemplate.query(sqlUserGetFriend, this::mapRowToUser, userId);
    }

    @Override
    public void userRemoveFriend(int userId, int friendId) {
        String sqlUserRemoveFriend = "DELETE FROM FRIENDSHIP WHERE USER1_ID=? AND USER2_ID=?";
        jdbcTemplate.update(sqlUserRemoveFriend, userId, friendId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("user_id"))
                .name(resultSet.getString("name"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    public Set<Integer> getFriendsIdListByUserId(int id) {
        String sqlGetFriendsIdListBuUserId = "SELECT USER2_ID FROM FRIENDSHIP WHERE USER1_ID = ?";
        return new HashSet<>(jdbcTemplate.query(sqlGetFriendsIdListBuUserId, (rs, friend_id) -> rs.getInt("user2_id"), id));
    }
}