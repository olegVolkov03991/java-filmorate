package ru.yandex.practicum.filmorate.validations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
@Component
public class FilmValidator {
	private final static LocalDate startFilmDate = LocalDate.of(1895, 12, 28);

	public static boolean validate(Film film) {
		if (film.getReleaseDate().isBefore(startFilmDate)) {
			log.error("ERROR");
			throw new ValidationException("Релиз раньше 28 декабря 1895 года");
		}
		return true;
	}
}
