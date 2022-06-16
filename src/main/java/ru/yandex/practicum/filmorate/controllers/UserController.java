package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;
import ru.yandex.practicum.filmorate.validations.UserValidator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")

public class UserController {

    private final Map<Long, User> users = new HashMap<>();
    private final UserValidator userValidator;
    private final IdGenerator idGenerator;

    @Autowired
    public UserController(IdGenerator idGenerator, UserValidator userValidator) {
        this.idGenerator = idGenerator;
        this.userValidator = userValidator;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("запрос получен к эндпоинту /users");
        user.setId(idGenerator.generator());
        if (userValidator.validate(user)) {
            log.error("Ошибка добавления: " + user.getName());
            return ResponseEntity.badRequest().body(user);
        }
        users.put(user.getId(), user);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user){
        log.info("Запрос получен к эндпоинту /users");
        try{
            if(user.getId() < 1){
                throw new ValidationException("user id less then 1");
            }
            users.put(user.getId(), user);
            log.debug("User update ", user.getId());
        } catch (ValidationException e){
            log.warn(e.getMessage());
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } return user;
    }

    @GetMapping
    public List<User> allUsers() {
        log.info("Запрос получен к эндпоинту /users");
        System.out.println("total users: " + users.size());
        return new ArrayList<>(users.values());
    }
}
