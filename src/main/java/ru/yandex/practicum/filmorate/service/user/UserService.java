package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userStorage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.validations.UserValidator;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

	private final InMemoryUserStorage userStorage;
	private final UserValidator userValidator;

	@Autowired
	public UserService(InMemoryUserStorage userStorage, UserValidator userValidator) {
		this.userStorage = userStorage;
		this.userValidator = userValidator;
	}

	public User createUser(User user) {
		userStorage.createUser(user);
		return user;
	}

	public User updateUser(User user) {
		userStorage.updateUser(user);
		return user;
	}

	public List<User> getAllUsers() {
		return userStorage.getAllUsers();
	}

	public void addFriend(Long userId, Long friendId) {
		log.info("запрос получен");
		userFound(userId);
		userFound(friendId);
		getUserById(userId).getFriends().add(friendId);
		getUserById(friendId).getFriends().add(userId);
	}

	public void userFound(Long userId) {
		if (userStorage.getUserById(userId) == null) {
			throw new UserNotFoundException(String.format("User not found"));
		}
	}

	public void deleteFriend(Long userId, Long friendId) {
		log.info("запрос получен");
		userFound(userId);
		userFound(friendId);
		userStorage.getUserById(userId).getFriends().remove(friendId);
		userStorage.getUserById(friendId).getFriends().remove(userId);
		log.info("friend remove");
	}

	public User getUserById(Long userId) {
		return userStorage.getUserById(userId);

	}

	public Set<User> getCommonFriends(long userId, long otherId) throws UserNotFoundException {
		Function<Long, User> userserFunction = (ind) -> {
			try {
				return userStorage.getUserById(ind);
			} catch (UserNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		};
		Set<Long> userFriends = userStorage.getUserById(userId).getFriends();
		Set<Long> otherFriends = userStorage.getUserById(otherId).getFriends();
		if (userFriends == null || otherFriends == null) {
			throw new UserNotFoundException(String.format("User not found"));
		}
		return userFriends.stream()
				.filter(fr -> otherFriends.contains(fr))
				.map(userserFunction)
				.collect(Collectors.toSet());
	}

	public List<User> userGetFriends(Long userId) {
		userFound(userId);
		return userStorage.userGetFriends(userId);
	}
}
