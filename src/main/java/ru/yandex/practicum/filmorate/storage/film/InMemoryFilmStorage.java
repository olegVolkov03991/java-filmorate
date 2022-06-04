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

    private final Map<Integer, Film> mapFilms = new HashMap<>();

    public Film create(int id, Film film){
        mapFilms.put(id, film);
        return mapFilms.get(id);
    }

    public Film update(int id, Film film){
       if(mapFilms.containsKey(id)){
           mapFilms.put(id, film);
           return mapFilms.get(id);
       } return null;
    }

    public List<Film> getAllFilms(){
        return new ArrayList<>(mapFilms.values());
    }

    @Override
    public User getFilmById(int id) {
        return null;
    }

}

