package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    public Film create(int id, Film film){
        films.put(id, film);
        return films.get(id);
    }

    public Film update(int id, Film film){
       if(films.containsKey(id)){
           films.put(id, film);
           return films.get(film);
       } return film;
    }

    public List<Film> getAllFilms(){
        return new ArrayList<>(films.values());
    }

}

