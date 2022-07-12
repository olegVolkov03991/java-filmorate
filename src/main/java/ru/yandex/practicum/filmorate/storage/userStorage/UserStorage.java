package ru.yandex.practicum.filmorate.storage.userStorage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

	User createUser(User user);

	User updateUser(User user) throws ValidationException;

	List<User> getAllUsers();
}
