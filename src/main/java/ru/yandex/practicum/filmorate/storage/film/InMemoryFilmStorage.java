package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    public Film create(long id, Film film){
        films.put(id, film);
        return films.get(id);
    }

    public Film update(long id, Film film){
       if(films.containsKey(id)){
           films.put(id, film);
           return films.get(film);
       } return film;
    }

    public List<Film> getAllFilms(){
        return (List<Film>) films.values();
    }

}

