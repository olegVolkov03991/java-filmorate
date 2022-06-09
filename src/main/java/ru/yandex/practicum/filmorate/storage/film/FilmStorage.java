package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film create(long id, Film film);

    Film update(long id, Film film);

    Collection<Film> getAllFilms();

}
