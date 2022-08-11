package ru.yandex.practicum.filmorate.validations;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
public class UserValidator {

	public boolean validate(User user){
		if(user.getName().isEmpty()){
			user.setName(user.getLogin());
		}
		if(user.getBirthday().isAfter(LocalDate.now())){
			throw new ValidationException("Проверьте дату рождения");
		}
		return false;
	}
}
