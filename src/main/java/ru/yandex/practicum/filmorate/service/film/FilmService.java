package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmDbStorage filmDbStorage;

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    public void createFilm(Film film) {
        filmDbStorage.createFilm(film).get();
    }

    public void updateFilm(Film film) {
        getFilmById(film.getId());
        filmDbStorage.updateFilm(film).get();
    }

    public List<Film> getAllFilm() {
        return filmDbStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        return filmDbStorage.getFilmById(id).orElseThrow(() -> new FilmNotFoundException("qwe"));
    }

    public void addLike(int film_id, int user_id) {
        filmDbStorage.addLike(film_id, user_id);
    }

    public void filmRemoveLike(int filmId, int userId) {
        filmDbStorage.filmRemoveLike(filmId, userId);
    }

    public List<Film> getPopularFilm(int count) {
        return filmDbStorage.getPopularFilm(count);
    }

    public void delete(int id) {
        filmDbStorage.delete(getFilmById(id));
    }
}
