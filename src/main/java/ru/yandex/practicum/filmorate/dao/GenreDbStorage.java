package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.storage.genreStorage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class GenreDbStorage implements GenreStorage {
    JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genres> getGenreById(int id) {
        String sqlGetGenresById = "select * from GENRES where GENRE_ID = ?";
        if (id < 0) {
            throw new FilmNotFoundException("negative id" + id);
        }
        List<Genres> genreRows = jdbcTemplate.query(sqlGetGenresById, this::makeGenre, id);
        if (genreRows.size() > 0) {
            Genres genre = genreRows.get(0);
            return Optional.of(genre);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Genres> getAllGenres() {
        String sqlGetGenresAll = "select * from genres";
        return jdbcTemplate.query(sqlGetGenresAll, this::makeGenre);
    }

    private Genres makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genres(rs.getInt("GENRE_ID"), rs.getString("NAME"));
    }
}
