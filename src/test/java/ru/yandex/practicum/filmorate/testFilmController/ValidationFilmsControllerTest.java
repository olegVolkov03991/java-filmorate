package ru.yandex.practicum.filmorate.testFilmController;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

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
            mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            log.info("Such a movie already exists or id has a negative value");
        }
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
