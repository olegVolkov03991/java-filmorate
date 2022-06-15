package ru.yandex.practicum.filmorate.validations;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Component
public class CheckValidFilm {
	private final LocalDate startFilmDate = LocalDate.of(1895, 12, 28);

	public void checkValidFilm(Film film, Boolean isCreated) {
		if (film.getReleaseDate().isBefore(startFilmDate)) {
			throw new ValidationException("Релиз раньше 28 декабря 1895 года");
		}
	}
}
