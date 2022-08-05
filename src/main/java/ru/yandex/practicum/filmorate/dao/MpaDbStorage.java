package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpaStorage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    String sqlMpaGet = "SELECT * FROM MPA WHERE MPA_ID=?";
    String sqlMpaGetAll = "SELECT * FROM MPA";

    @Override
    public Mpa MpaGet(int id) {
        if(id<0){
            throw new FilmNotFoundException("negative id");
        }return jdbcTemplate.queryForObject(sqlMpaGet, this::mapRowToMpa, id);
    }

    public List<Mpa> mpaGetAll() {
        return jdbcTemplate.query(sqlMpaGetAll, this::mapRowToMpa);
    }

    private Mpa mapRowToMpa(ResultSet relustSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(relustSet.getInt("mpa_id"))
                .name(relustSet.getString("name"))
                .build();
    }
}