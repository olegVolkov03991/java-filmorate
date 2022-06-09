package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private final LocalDate startFilmDate = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;

    public FilmController(FilmStorage filmStorage){
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public Film create(@RequestBody Film film){
        log.info("Запрос получен к эндпоинту /film");
        checkValidFilm(film, true);
        if(filmStorage.create(film.getId(), film)!=null && film.getId()>0){
           // film.setId(film.getId());
            films.put(film.getId(), film);
            return film;
        } else{
            throw new NotFoundObjectException("Такой фильм уже есть или id имеет отрицательное значение");
        }
    }

    @PutMapping
    public ResponseEntity<Film> update(@Validated @RequestBody Film film) throws ValidationException{
        if(films.containsKey(film.getId())){
            log.debug("Updating film data ID "+film.getId());
            films.put(film.getId(), film);
        }else {
            throw new ValidationException(String.format("Update dailed: user with ID" , film.getId()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok(film);
    }

    @GetMapping
    public List<Film> allFilm(){
        return filmStorage.getAllFilms();
    }
//    public ResponseEntity<Collection<Film>>getFilms(){
//        log.debug("Returned film list "+ films.values());
//        return ResponseEntity.ok(films.values());


    public void checkValidFilm(Film film, Boolean isCreated){
        if(isCreated){
            for(Film getFilm:filmStorage.getAllFilms()){
                if(film.getName().equals(getFilm.getName())&&film.getReleaseDate().equals(film.getReleaseDate())){
                    throw new ValidationException("такой фильм уже есть", HttpStatus.INTERNAL_SERVER_ERROR);
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
