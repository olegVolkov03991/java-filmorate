package ru.yandex.practicum.filmorate.storage.userStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.idGenerator.IdGeneratorUser;
import ru.yandex.practicum.filmorate.validations.UserValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
	private final Map<Long, User> users = new HashMap<>();
	private final UserValidator userValidator;
	private IdGeneratorUser idGenerator;

	@Autowired
	public InMemoryUserStorage(UserValidator userValidator, IdGeneratorUser idGenerator) {
		this.userValidator = userValidator;
		this.idGenerator = idGenerator;
	}

	public User createUser(@Valid @RequestBody User user) {
		log.info("запрос получен");
		userValidator.validate(user);
		user.setId(idGenerator.generator());
		userValidator.validate(user);
		if (users.containsKey(user.getId())) {
			throw new UserAlreadyExistException(String.format("позователь с " + user.getLogin() + " логином уже есть"));
		}
		users.put(user.getId(), user);
		return user;
	}

	public User updateUser(@Valid @RequestBody User user) {
		log.info("запрос получен");
		userValidator.validate(user);
		if (getUserById(user.getId()) == null) {
			throw new UserNotFoundException(String.format("user not found"));
		}
		users.put(user.getId(), user);
		return user;
	}

	public List<User> getAllUsers() {
		log.info("запрос получен");
		System.out.println(users);
		return new ArrayList<>(users.values());
	}

	public List<User> userGetFriends(Long id) {
		log.info("запрос получен");
		if (getUserById(id) == null) {
			throw new UserNotFoundException(String.format("пользователь не найден"));
		}
		return users.get(id).getFriends().stream()
				.map(users::get)
				.collect(Collectors.toList());
	}

	public User getUserById(Long id) {
		log.info("запрос получен");
		if (!users.containsKey(id)) {
			throw new UserNotFoundException(String.format("пользователь не найден"));
		}
		return users.get(id);
	}
}
