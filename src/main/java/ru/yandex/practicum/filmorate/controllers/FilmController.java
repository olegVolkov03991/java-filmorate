package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.NotFoundObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int id;
    private final Map<Integer, Film> films = new HashMap<>();
    private final LocalDate startFilmDate = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;

    public FilmController(FilmStorage filmStorage){
        this.filmStorage = filmStorage;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film){
        log.info("Запрос получен к эндпоинту /films");
        id++;
        film.setId(id);
        films.put(id, film);
        log.debug("Add id: ", film.getId());
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film){
        try {
            if(film.getId() < 1){
                throw new ValidationException("Film id less then 1", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            films.put(film.getId(), film);
            log.debug("Film updated  ", film.getId());
        } catch (ValidationException e){
            log.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } return film;
    }

    @GetMapping("/films")
    public Collection<Film> allFilms(){
        log.info("Запрос получен к эндпоинту /films");
        return new ArrayList<>(films.values());
    }

    public void checkValidFilm(Film film, Boolean isCreated){
        if(isCreated){
            for(Film getFilm:filmStorage.getAllFilms()){
                if(film.getName().equals(getFilm.getName())&&film.getReleaseDate().equals(film.getReleaseDate())){
                    throw new ValidationException("Такой фильм уже есть", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }

        if(film.getReleaseDate().isBefore(startFilmDate)){
            throw new ValidationException("Релиз раньше 28 декабря 1895 года", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(film.getDuration()<=0){
            throw new ValidationException("Отрицательная продолжительность фильма", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(film.getName().isEmpty()){
            throw new ValidationException("Имя не может быть пустым", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(film.getDescription().length() > 200){
            throw new ValidationException("Описание не может привышать 200 символов", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
