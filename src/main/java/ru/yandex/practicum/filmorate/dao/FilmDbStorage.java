package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.filmStorage.FilmStorage;
import ru.yandex.practicum.filmorate.validations.FilmValidator;

import javax.validation.Valid;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Primary
@Component
public class FilmDbStorage implements FilmStorage {

    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmValidator filmValidator) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmValidator = filmValidator;
    }

    private final JdbcTemplate jdbcTemplate;
    private final FilmValidator filmValidator;

    @Override
    public Optional<Film> createFilm(@Valid Film film) {
        try {
            filmValidator.validate(film);
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("film")
                    .usingGeneratedKeyColumns("film_id");
            film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());
            film.getGenres().forEach(genre -> addFilmsGenre(film.getId(), genre.getId()));
            film.setGenres(new LinkedHashSet<>(getFilmGenres(film.getId())));
        } catch (Exception e){
            throw new ValidationException("Error program");
        } return Optional.of(film);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        String sqlUpdateFilm = "UPDATE FILM SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID=? WHERE FILM_ID = ?";
        if (getFilmById(film.getId()).isEmpty()) {
            throw new FilmNotFoundException(String.format("Film with id %s not found", film.getId()));
        }
        jdbcTemplate.update(sqlUpdateFilm,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        deleteFilmGenres(film.getId());
        film.getGenres().forEach(genre -> addFilmsGenre(film.getId(), genre.getId()));
        film.setGenres(new LinkedHashSet<>(getFilmGenres(film.getId())));
        return Optional.of(film);
    }

    public List<Film> getPopularFilm(int count) {
        String sqlGetPopularFilm = "SELECT * FROM FILM LEFT JOIN LIKES ON LIKES.FILM_ID = FILM.FILM_ID GROUP BY FILM.FILM_ID ORDER BY COUNT(LIKES.USER_ID) DESC LIMIT ?";
        return jdbcTemplate.query(sqlGetPopularFilm, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Mpa mpa = getSetMpa(rs);
        Film film = new Film(id, name, mpa, description, releaseDate, duration);
        film.setGenres(new LinkedHashSet<>(getFilmGenres(id)));
        return film;
    }

    private Mpa getSetMpa(ResultSet rs) throws SQLException {
        String sqlGetSetMpa = "SELECT * FROM MPA WHERE MPA_ID = ?";
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("mpa_id"));
        mpa.setName(Objects.requireNonNull(jdbcTemplate.queryForObject(sqlGetSetMpa, this::mapRowToMpa, mpa.getId())).getName());
        return mpa;
    }

    private Mpa mapRowToMpa(ResultSet relustSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(relustSet.getInt("mpa_id"))
                .name(relustSet.getString("name"))
                .build();
    }

    @Override
    public void delete(Film film) {
        String sqlDelete = "DELETE FROM FILM WHERE FILM_ID = ?";
        if (jdbcTemplate.update(sqlDelete, film.getId()) > 0) {
            log.info("Фильм с ID {} был удален", film.getId());
        } else throw new RuntimeException(String.format("Film with id %s not found", film.getId()));
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sqlLike = "INSERT INTO LIKES(FILM_ID, USER_ID) " + "VALUES(?,?)";
        jdbcTemplate.update(sqlLike, filmId, userId);
    }

    @Override
    public void filmRemoveLike(int filmId, int userId) {
        String sqlDeleteLike = "DELETE FROM LIKES WHERE FILM_ID=? AND USER_ID=?";
        if (getFilmById(filmId).isEmpty()) {
           throw new FilmNotFoundException(String.format("Film with id %s not found", filmId));
        }
        if (userId < 0) {
            throw new UserNotFoundException(String.format("Film with id %s not found", filmId));
        }
        jdbcTemplate.update(sqlDeleteLike, filmId, userId);
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlGetAllFilm = "SELECT * FROM FILM";
        return jdbcTemplate.query(sqlGetAllFilm, this::mapRowToFilm);
    }

    public Optional<Film> getFilmById(int id) {
        String sqlGetFilmById = "SELECT * FROM FILM WHERE FILM_ID = ?";
        if (id < 0) {
            log.error(String.format("Film with id %s is negative", id));
        }
        List<Film> filmRows = jdbcTemplate.query(sqlGetFilmById, this::mapRowToFilm, id);
        if (filmRows.size() > 0) {
            Film film = filmRows.get(0);
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    public void deleteFilmGenres(int filmID) {
        String sqlDeleteFilmGenres = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlDeleteFilmGenres, filmID);
    }

    public void addFilmsGenre(int filmId, int genreId) {
        String sqlAddFilmsGenre = "MERGE INTO FILM_GENRE(FILM_ID, GENRE_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlAddFilmsGenre, filmId, genreId);
    }

    public Set<Genres> getFilmGenres(int filmID) {
        String sqlQueryGenreId = "SELECT GENRE_ID FROM FILM_GENRE WHERE FILM_ID = ?";
        String sqlQueryGenreFull = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        Set<Genres> resultList = new TreeSet<>(Comparator.comparingInt(Genres::getId));
        List<Integer> listOfGenres = jdbcTemplate.queryForList(sqlQueryGenreId, Integer.class, filmID);
        for (int genreId : listOfGenres) {
            resultList.add(jdbcTemplate.query(sqlQueryGenreFull, new GenreMapper(), genreId).
                    stream().findFirst().orElse(null));
        }
        return resultList;
    }
}