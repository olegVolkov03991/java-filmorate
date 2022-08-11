package ru.yandex.practicum.filmorate.storage.mpaStorage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    Mpa MpaGet(int mpaId);
    List<Mpa> mpaGetAll();
}
