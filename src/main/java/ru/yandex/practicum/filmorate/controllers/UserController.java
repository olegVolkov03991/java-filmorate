package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")

public class UserController {
    private long id;

    Map<Long, User> users = new HashMap<>();

    private final UserStorage userStorage;

    public UserController(UserStorage userStorage){
        this.userStorage = userStorage;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("запрос получен к эндпоинту /users");
        checkValidUser(user, true);
        id++;
        user.setId(id);
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
            throw new Exception("Оштбка сервера");
        }
        if (!users.containsKey(user.getId())) {
            log.info("ошибка обновления: " + user.getId());
        }
        users.put(user.getId(), user);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public ArrayList<User> allUsers() {
        return new ArrayList<>(users.values());
    }

    private void checkValidUser(User user, Boolean isCreated){
        if(isCreated){
            for(User getUser : userStorage.getAllUsers()){
                if(user.getEmail().equals(getUser.getEmail())){
                    throw new ValidationException("Такой пользователь уже есть", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        if(user.getName()==null||user.getName().isBlank()||user.getName().isEmpty()){
            user.setName(user.getLogin());
        }
        if(user.getLogin().isEmpty() || user.getLogin().isBlank()){
            throw new ValidationException("Проверьте логин", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(user.getBirthday().isAfter(LocalDate.now())){
            throw new ValidationException("Проверьте дату рождения", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(user.getEmail().isEmpty() || !user.getEmail().contains("@")){
            throw new ValidationException("Проверьте почту", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
