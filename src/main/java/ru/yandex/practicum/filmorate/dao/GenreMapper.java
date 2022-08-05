package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<Genres> {

    @Override
    public Genres mapRow(ResultSet rs, int rowNum) throws SQLException {
        Genres genre = new Genres();
        genre.setId(rs.getInt("GENRE_ID"));
        genre.setName(rs.getString("NAME"));
        return genre;
    }
}
