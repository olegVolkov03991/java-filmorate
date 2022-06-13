package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.swing.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
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
    public User create(@Valid @RequestBody User user){
        log.info("запрос получен к эндпоинту /users");
        id++;
        user.setId(id);
        users.put(id, user);
        log.debug("Add id:", user.getId());
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user){
        log.info("получен запрос к энпоинту /users");
        checkValidUser(user, false);
        if(userStorage.update(user.getId(), user)!=null && user.getId()>0){
            return userStorage.getUserById(user.getId());
        } else{
            throw new NotFoundObjectException("Такого пользователя нет или id отрицательный");
        }
    }

    @GetMapping
    public List<User> allUsers(){
        return List.copyOf(users.values());
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
