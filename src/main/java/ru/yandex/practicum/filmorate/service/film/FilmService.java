package ru.yandex.practicum.filmorate.service.film;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.filmStorage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.userStorage.InMemoryUserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
	private final InMemoryFilmStorage filmStorage;
	private final InMemoryUserStorage userStorage;

	@Autowired
	public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
		this.filmStorage = filmStorage;
		this.userStorage = userStorage;
	}

	public Film createFilm(Film film) {
		filmStorage.createFilm(film);
		return film;
	}

	public Film UpdateFilm(Film film) {
		filmStorage.updateFilm(film);
		return film;
	}

	public List<Film> getAllFilm() {
		return filmStorage.getAllFilms();
	}

	public void addLike(Long id, Long userId) {
		if (!filmStorage.getAllFilms().contains(id)) {
			log.error("film not found");
		}
		if (userStorage.getAllUsers().contains(userId)) {
			filmStorage.getAllFilms().get(Math.toIntExact(id)).getLikes().add(userId);
		}
		log.info("ok");
	}

	public void deleteLike(Long id, Long userId) {
		if (filmStorage.getFilmById(id) == null) {
			throw new FilmNotFoundException(String.format("film not found"));
		}
		if (userStorage.getUserById(userId) == null) {
			throw new UserNotFoundException(String.format("user not found"));
		}
		filmStorage.getFilmById(id).getLikes().remove(userId);
		log.info("like remove");
	}

	public Film getFilmById(Long id) {
		return filmStorage.getFilmById(id);

	}

	public List<Film> getTopFilm(long count) {
		if (count < 1) {
			log.info("count < 1");
		}
		Comparator<Film> filmComparator = Comparator.comparing(film -> film.getLikes().size());
		filmComparator = filmComparator.thenComparing(Film::getId);
		return filmStorage.getAllFilms().stream()
				.sorted(filmComparator.reversed())
				.limit(count)
				.collect(Collectors.toList());
	}
}
