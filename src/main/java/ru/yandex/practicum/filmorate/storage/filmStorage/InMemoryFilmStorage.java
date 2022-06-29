package ru.yandex.practicum.filmorate.storage.filmStorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistEcxeption;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validations.FilmValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
	private final FilmValidator filmValidator;
	private final Map<Long, Film> films = new HashMap<>();

	@Autowired
	public InMemoryFilmStorage(FilmValidator filmValidator) {
		this.filmValidator = filmValidator;
	}

	@Override
	public Film createFilm(Film film) {
		log.info("Запрос получен");
		film.setId(Long.valueOf(films.size() + 1));
		filmValidator.validate(film);
		if (films.containsKey(film.getId())) {
			throw new FilmAlreadyExistEcxeption(String.format("film Already"));
		}
		films.put(film.getId(), film);
		return film;
	}

	@Override
	public Film updateFilm(@Valid @RequestBody Film film) {
		log.info("Запрос получен");
		filmValidator.validate(film);
		if (getFilmById(film.getId()) == null) {
			throw new FilmNotFoundException(String.format("film not found"));
		}
		films.put(film.getId(), film);
		return film;
	}

	@Override
	public List<Film> getAllFilms() {
		log.info("Запрос получен");
		System.out.println("total films: " + films.size());
		return new ArrayList<>(films.values());
	}

	public Film getFilmById(Long id){
		log.info("   ");
		if (!films.containsKey(id)) {
			throw new FilmNotFoundException(String.format("film not found"));
		}
		return films.get(id);
	}
}
