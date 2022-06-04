package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> mapUsers = new HashMap<>();

    public User create(int id, User user){
        mapUsers.put(id, user);
        return mapUsers.get(id);
    }

    public User update(int id, User user){
        if(mapUsers.containsKey(id)){
            mapUsers.put(id, user);
            return mapUsers.get(id);
        } return null;
    }

    public List<User> getAllUsers(){
        return new ArrayList<>(mapUsers.values());
    }

    @Override
    public User getUserById(int id) {
        return null;
    }
}
