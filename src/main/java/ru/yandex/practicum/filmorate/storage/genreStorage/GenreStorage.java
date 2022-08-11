package ru.yandex.practicum.filmorate.storage.genreStorage;

import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Optional<Genres> getGenreById(int id);
    List<Genres> getAllGenres();
}
