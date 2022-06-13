package ru.yandex.practicum.filmorate.testFilmController;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class ValidationFilmsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    Film film;
    Film film1;
    Film film2;
    FilmStorage filmStorage = new InMemoryFilmStorage();
    Map<Long, Film> map = new HashMap<>();
    FilmController filmController = new FilmController(filmStorage);

    @Test
    public void TestCreateFilm() throws Exception {
        film = Film.builder()
                .id(1)
                .name("qwe 1")
                .description("This is horror")
                .releaseDate(LocalDate.of(2010, 12, 23))
                .duration(120)
                .build();
        String body = objectMapper.writeValueAsString(film);
        try {
            filmController.create(film);
            mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            log.info("Such a movie already exists or id has a negative value");
        }
    }

//    public void TestCreateUser() throws Exception {
//        user = User.builder()
//                .id(1)
//                .name("qwe")
//                .birthday(LocalDate.of(2010, 12, 22))
//                .email("qweqweqwe@yandex.ru")
//                .login("qwe")
//                .build();
//        String body = objectMapper.writeValueAsString(user);
//        try {
//            mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk());
//        } catch (Exception e){
//            log.info("Error: This user already exists");
//        }
//    }

    @Test
    public void TestGetAllFilm() throws Exception{
        film = Film.builder()
                .id(1)
                .name("qwe 1")
                .description("This is horror")
                .releaseDate(LocalDate.of(2010, 12, 23))
                .duration(120)
                .build();

        film1 = Film.builder()
                .id(1)
                .name("qe 1")
                .description("Ts is horror")
                .releaseDate(LocalDate.of(2010, 12, 23))
                .duration(120)
                .build();
        film2 = Film.builder()
                .id(3)
                .name("q 1")
                .description("Tis horror")
                .releaseDate(LocalDate.of(2010, 12, 23))
                .duration(120)
                .build();
//        map.put(film.getId(), film);
//        map.put(film1.getId(), film1);
//        System.out.println(map);
//        System.out.println("///////");
//        System.out.println(map.values());
//        System.out.println("////////");
//       filmStorage.create(film1.getId(), film1);
//       filmStorage.create(film.getId(), film);
//        System.out.println(filmStorage.getAllFilms());
//        System.out.println("////////////////////");
        filmController.create(film2);
      //  filmController.create(film);
        filmController.create(film1);
        System.out.println(filmController.allFilms());


    }

    @Test
    public void TestUpdateFilm()throws Exception{
        film = Film.builder()
                .id(1)
                .name("qwe")
                .description("This is horror")
                .releaseDate(LocalDate.of(2010, 12, 23))
                .duration(120)
                .build();
        HashMap<Integer, Film> films = new HashMap<>();
        films.put(1, film);
        film1 = Film.builder()
                .id(1)
                .name("rty")
                .description("This is ")
                .releaseDate(LocalDate.of(2010, 12, 23))
                .duration(120)
                .build();

        String body = objectMapper.writeValueAsString(film);


    }

    @Test
    public void TestDuration() throws Exception {
        film = Film.builder()
                .id(1)
                .name("йцу")
                .description("This is horror")
                .releaseDate(LocalDate.of(2010, 12, 23))
                .duration(-120)
                .build();
        String body = objectMapper.writeValueAsString(film);
        try {
            mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            log.info("Error: negative film length");
        }
    }

    @Test
    public void TestRelease() throws Exception {
        film = Film.builder()
                .id(1)
                .name("qwe 1")
                .description("This is horror")
                .releaseDate(LocalDate.of(1800, 12, 23))
                .duration(120)
                .build();
        String body = objectMapper.writeValueAsString(film);
        try {
            mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            log.info("Error: release before December 28, 1895");
        }
    }

    @Test
    public void TestForAnEmptyName() throws Exception {
        film = Film.builder()
                .id(1)
                .name("")
                .description("This is horror")
                .releaseDate(LocalDate.of(2010, 12, 23))
                .duration(120)
                .build();
        String body = objectMapper.writeValueAsString(film);
        try {
            mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            log.info("Error: name cannot be empty");
        }
    }

    @Test
    public void durationTest() throws Exception {
        film = Film.builder()
                .id(1)
                .name("qee")
                .description("11111111111111111111" +
                        "11111111111111111111" +
                        "11111111111111111111" +
                        "11111111111111111111" +
                        "11111111111111111111" +
                        "11111111111111111111" +
                        "11111111111111111111" +
                        "11111111111111111111" +
                        "11111111111111111111" +
                        "11111111111111111111" +
                        "11111111111111111111" +
                        "11111111111111111111")
                .releaseDate(LocalDate.of(2010, 12, 23))
                .duration(120)
                .build();
        String body = objectMapper.writeValueAsString(film);
        try {
            mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            log.info("Error: description cannot exceed 200 characters");
        }
    }
}
