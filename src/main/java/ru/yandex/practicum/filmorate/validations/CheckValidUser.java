package ru.yandex.practicum.filmorate.validations;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
public class CheckValidUser {

	public void checkValidUser(User user, Boolean isCreated){
		if(user.getName()==null||user.getName().isBlank()||user.getName().isEmpty()){
			user.setName(user.getLogin());
		}
		if(user.getLogin().isEmpty() || user.getLogin().isBlank()){
			throw new ValidationException("Проверьте логин");
		}
		if(user.getBirthday().isAfter(LocalDate.now())){
			throw new ValidationException("Проверьте дату рождения");
		}
		if(user.getEmail().isEmpty() || !user.getEmail().contains("@")){
			throw new ValidationException("Проверьте почту");
		}
	}
}
