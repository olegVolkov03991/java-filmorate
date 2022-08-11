package ru.yandex.practicum.filmorate.storage.filmStorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> createFilm(Film film);

    Optional<Film> updateFilm(Film film);

    List<Film> getAllFilms();

    Optional<Film> getFilmById(int id);

    void addLike(int filmId, int userId);

    void filmRemoveLike(int filmId, int userId);

    List<Film> getPopularFilm(int count);

    void delete(Film film);
}
