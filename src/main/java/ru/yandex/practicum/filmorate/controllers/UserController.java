package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundObjectException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")

public class UserController {
    private int id;

    private final UserStorage userStorage;

    public UserController(UserStorage userStorage){
        this.userStorage = userStorage;
    }

    @PostMapping
    public User create(@RequestBody User user){
        log.info("запрос получен к эндпоинту /user");
        checkValidUser(user, true);
        if(userStorage.create(id, user)!=null || user.getId()<0){
            user.setId(id);
            return null;
        } else {
            throw new NotFoundObjectException("такой пользователь уже есть или id имеет отрицательное значение");
        }
    }

    @PutMapping
    public User update(@RequestBody User user){
        log.info("получен запрос к энпоинту /user");
        checkValidUser(user, false);
        if(userStorage.update(user.getId(), user)!=null && user.getId()>0){
            return userStorage.getUserById(user.getId());
        } else{
            throw new NotFoundObjectException("Такого пользователя нет или id отрицательный");
        }
    }

    @GetMapping
    public List<User> allUsers(){
        return userStorage.getAllUsers();
    }

    private void checkValidUser(User user, Boolean isCreated){
        if(isCreated){
            for(User getUser : userStorage.getAllUsers()){
                if(user.getEmail().equals(getUser.getEmail())){
                    throw new ValidationException("Такой пользователь уже есть");
                }
            }
        }
        if(user.getName()==null||user.getName().isBlank()||user.getName().isEmpty()){
            user.setName(user.getLogin());
        }
        if(user.getLogin().isEmpty() || user.getLogin().isBlank()){
            throw new ValidationException("Проверьте логин");
        }
        if(user.getBirthday().isAfter(LocalDate.now())){
            throw new ValidationException("Проверьте дату рождения");
        }
        if(user.getEmail().isEmpty() || !user.getEmail().contains("@")){
            throw new ValidationException("Проверьте почту");
        }
    }
}
