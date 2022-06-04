package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(int id, User user);

    User update(int id, User user);

    List<User> getAllUsers();

    User getUserById(int id);
}
