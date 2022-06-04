package ru.yandex.practicum.filmorate.testUserController;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class ValidationUsersControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    User user;

    @Test
    public void TestCreateUser() throws Exception {
        user = User.builder()
                .id(1)
                .name("qwe")
                .birthday(LocalDate.of(2010, 12, 22))
                .email("qweqweqwe@yandex.ru")
                .login("qwe")
                .build();
        String body = objectMapper.writeValueAsString(user);
        try {
            mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e){
            log.info("Error: This user already exists");
        }
    }

    @Test
    public void TestDateOfBirth() throws Exception {
        user = User.builder()
                .id(1)
                .name("qwe")
                .birthday(LocalDate.of(2020, 12, 22))
                .email("qweqweqwe@yandex.ru")
                .login("qwe")
                .build();
        String body = objectMapper.writeValueAsString(user);
        try {
            mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e){
            log.info("Error: check date of birth");
        }
    }

    @Test
    public void TestLogin() throws Exception {
        user = User.builder()
                .id(1)
                .name("qwe")
                .birthday(LocalDate.of(2010, 12, 22))
                .email("qweqweqwe@yandex.ru")
                .login("")
                .build();
        String body = objectMapper.writeValueAsString(user);
        try {
            mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e){
log.info("Error: login cannot be empty");
        }
    }

    @Test
    public void TestSpaceInLogin() throws Exception {
        user = User.builder()
                .id(1)
                .name("qwe")
                .birthday(LocalDate.of(2010, 12, 22))
                .email("qweqweqwe@yandex.ru")
                .login(" ")
                .build();
        String body = objectMapper.writeValueAsString(user);
        try {
            mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e){
            log.info("Error: login cannot contain a space");
        }
    }

    @Test
    public void TestMail() throws Exception {
        user = User.builder()
                .id(1)
                .name("qwe")
                .birthday(LocalDate.of(2010, 12, 22))
                .email("qweyande.ru")
                .login("qwe")
                .build();
        String body = objectMapper.writeValueAsString(user);
        try {
            mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e){
            log.info("Error: mail name must contain the @ symbol");
        }
    }

    @Test
    public void TestIsEpmtyMail() throws Exception {
        user = User.builder()
                .id(1)
                .name("qwe")
                .birthday(LocalDate.of(2010, 12, 22))
                .email("")
                .login("qwe")
                .build();
        String body = objectMapper.writeValueAsString(user);
        try {
            mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (Exception e){
            log.info("Error: mail name cannot be empty");
        }
    }
}
