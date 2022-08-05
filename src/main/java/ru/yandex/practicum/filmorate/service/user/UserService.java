package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserDbStorage userDbStorage;

    @Autowired
    public UserService(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public User createUser(User user) {
        userDbStorage.createUser(user);
        return user;
    }

    public User updateUser(User user) {
        userDbStorage.updateUser(user);
        return user;
    }

    public List<User> getAllUsers() {
        return userDbStorage.getAllUsers();
    }

    public User getUserById(int id) {
        return userDbStorage.getUserById(id);
    }

    public void addFriend(int userId, int friendId) {
        userDbStorage.userAddFriend(userId, friendId);
    }

    public List<User> userGetFrined(int id) {
        return userDbStorage.userGetFriend(id);
    }

    public void userRemoveFriend(int id, int friendId) {
        userDbStorage.userRemoveFriend(id, friendId);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        List<User> commonFriends = new ArrayList<>();
        Set<Integer> userFriends = new HashSet<>(userDbStorage.getFriendsIdListByUserId(userId));
        Set<Integer> otherFriends = new HashSet<>(userDbStorage.getFriendsIdListByUserId(otherId));
        userFriends.retainAll(otherFriends);
        for (int userFriendsId : userFriends) {
            commonFriends.add(userDbStorage.getUserById(userFriendsId));
        }
        return commonFriends;
    }
}
