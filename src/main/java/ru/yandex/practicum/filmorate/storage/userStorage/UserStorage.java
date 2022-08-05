package ru.yandex.practicum.filmorate.storage.userStorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    void createUser(User user);

    void updateUser(User user);

    List<User> getAllUsers();

    User getUserById(int id);

    void userAddFriend(int user_id, int friend_id);

    List<User> userGetFriend(int user_id);

    void userRemoveFriend(int id, int friendId);

    Set<Integer> getFriendsIdListByUserId(int id);
}
