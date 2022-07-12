package ru.yandex.practicum.filmorate.validations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
@Component
public class FilmValidator {
	public final static LocalDate START_FILM_DATE = LocalDate.of(1895, 12, 28);

	public boolean validate(Film film) {
		if (film.getReleaseDate().isBefore(START_FILM_DATE)) {
			throw new ValidationException("Релиз раньше 28 декабря 1895 года");
		}
		return true;
	}
}
