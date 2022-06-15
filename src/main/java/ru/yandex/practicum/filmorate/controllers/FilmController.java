package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.validations.CheckValidFilm;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.IdGenerator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final IdGenerator id = new IdGenerator();
    private final Map<Integer, Film> films = new HashMap<>();
    private final CheckValidFilm checkValidFilm;

    @Autowired
    public FilmController(CheckValidFilm checkValidFilm) {
        this.checkValidFilm = checkValidFilm;
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film){
        checkValidFilm.checkValidFilm(film);
        log.info("Запрос получен к эндпоинту /films");
        film.setId(id.generator());
        if(films.containsKey(film.getId())){
            log.info("ошибка добавления: " + film.getName());
            return ResponseEntity.badRequest().body(film);
        }
        films.put(film.getId(), film);
        return ResponseEntity.ok().body(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film){
        try {
            if(film.getId() < 1){
                throw new ValidationException("Film id less then 1");
            }
            films.put(film.getId(), film);
            log.debug("Film updated  ", film.getId());
        } catch (ValidationException e){
            log.warn(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } return film;
    }

    @GetMapping
    public Collection<Film> allFilms(){
        log.info("Запрос получен к эндпоинту /films");
        System.out.println("total films: " + films.size());
        return new ArrayList<>(films.values());
    }
}
