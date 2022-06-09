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

    private final Map<Long, Film> Films = new HashMap<>();

    public Film create(long id, Film film){
        Films.put(id, film);
        return Films.get(id);
    }

    public Film update(long id, Film film){
       if(Films.containsKey(id)){
           Films.put(id, film);
           return Films.get(film);
       } return film;
    }

    public List<Film> getAllFilms(){
        return new ArrayList<>(Films.values());
    }

}

