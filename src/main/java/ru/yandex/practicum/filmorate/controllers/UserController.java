package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.IdGenerator;
import ru.yandex.practicum.filmorate.validations.CheckValidUser;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")

public class UserController {

    private final IdGenerator id = new IdGenerator();
    private final Map<Long, User> users = new HashMap<>();
    private final CheckValidUser checkValidUser;

    @Autowired
    public UserController(CheckValidUser checkValidUser) {
        this.checkValidUser = checkValidUser;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("запрос получен к эндпоинту /users");
        checkValidUser.checkValidUser(user);
        user.setId(id.generator());
        if (users.containsKey(user.getId())) {
            log.info("Ошибка добавления: " + user.getName());
            return ResponseEntity.badRequest().body(user);
        }
        users.put(user.getId(), user);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) throws Exception {
        log.info("получен запрос к энпоинту /users");
        if (user.getId() < 0) {
            throw new Exception("Ошибка сервера");
        }
        if (!users.containsKey(user.getId())) {
            log.info("Ошибка обновления: " + user.getId());
        }
        users.put(user.getId(), user);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public List<User> allUsers() {
        return new ArrayList<>(users.values());
    }
}
