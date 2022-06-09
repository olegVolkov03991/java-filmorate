package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(long id, User user);

    User update(long id, User user);

    List<User> getAllUsers();

    User getUserById(long id);

}
