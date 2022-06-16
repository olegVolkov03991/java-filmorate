package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.IdGenerator;
import ru.yandex.practicum.filmorate.validations.FilmValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")

public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private final FilmValidator filmValidator;
    private final IdGenerator idGenerator;

    @Autowired
    public FilmController(IdGenerator idGenerator, FilmValidator filmValidator) {
        this.idGenerator = idGenerator;
        this.filmValidator = filmValidator;
    }

    @PostMapping
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        log.info("Запрос получен к эндпоинту /films");
        filmValidator.validate(film);
        film.setId(idGenerator.generator());
        if (films.containsKey(film.getId())) {
            log.info("ошибка добавления: " + film.getName());
            return ResponseEntity.badRequest().body(film);
        }
        films.put(film.getId(), film);
        return ResponseEntity.ok().body(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film){
        log.info("Запрос получен к эндпоинту /films");
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
        System.out.println("total films: " + films.size() + films.values());
        return new ArrayList<>(films.values());
    }
}
