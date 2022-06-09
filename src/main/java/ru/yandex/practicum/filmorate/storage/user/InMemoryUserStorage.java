package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    public User create(long id, User user){
        users.put(id, user);
        return users.get(id);
    }

    public User update(long id, User user){
        if(users.containsKey(id)){
            users.put(id, user);
            return users.get(id);
        } return null;
    }

    public List<User> getAllUsers(){
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(long id) {
        return null;
    }
}
