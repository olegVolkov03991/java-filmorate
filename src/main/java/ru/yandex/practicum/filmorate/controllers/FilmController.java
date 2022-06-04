package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int id = 1;
    private final LocalDate startFilmDate = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;

    public FilmController(FilmStorage filmStorage){
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public Film create(@RequestBody Film film){
        log.info("Запрос получен к эндпоинту /film");
        checkValidFilm(film, true);
        if(filmStorage.create(id, film)!=null && film.getId()>0){
            film.setId(id);
            return null;
        } else{
            throw new NotFoundObjectException("Такой фильм уже есть или id имеет отрицательное значение");
        }
    }

    @PutMapping
    public User update(@RequestBody Film film){
        log.info("Получен запрос к эндпоинту /film");
        checkValidFilm(film, false);
        if(filmStorage.update(film.getId(), film)!=null && film.getId() > 0){
            return filmStorage.getFilmById(film.getId());
        } else{
            throw new NotFoundObjectException("Такого фильма нет или id имеет отрицательное значение");
        }
    }

    @GetMapping
    public List<Film> allFilms(){
        return (List<Film>) filmStorage.getAllFilms();
    }

    private void checkValidFilm(Film film, Boolean isCreated){
        if(isCreated){
            for(Film getFilm:filmStorage.getAllFilms()){
                if(film.getName().equals(getFilm.getName())&&film.getReleaseDate().equals(film.getReleaseDate())){
                    throw new ValidationException("такой фильм уже есть");
                }
            }
        }

        if(film.getReleaseDate().isBefore(startFilmDate)){
            throw new ValidationException("Релиз раньше 28 декабря 1895 года");
        }
        if(film.getDuration()<=0){
            throw new ValidationException("Отрицательная продолжительность фильма");
        }
        if(film.getName().isEmpty()){
            throw new ValidationException("Имя не может быть пустым");
        }

        if(film.getDescription().length() > 200){
            throw new ValidationException("Описание не может привышать 200 символов");
        }
    }
}
