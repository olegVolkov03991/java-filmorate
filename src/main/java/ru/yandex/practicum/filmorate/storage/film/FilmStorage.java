package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Film create(int id, Film film);

    List<Film> getAllFilms();

    Film update(int id, Film film);

}
