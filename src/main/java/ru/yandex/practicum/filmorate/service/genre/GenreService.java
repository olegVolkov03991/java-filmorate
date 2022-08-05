package ru.yandex.practicum.filmorate.service.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

@Service
public class GenreService {
    GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public Genres genreGetByID (int id){
        return genreDbStorage.getGenreByID(id).get();
    }

    public List<Genres> genreGetAll(){
        return genreDbStorage.genreGetAll();
    }
}
