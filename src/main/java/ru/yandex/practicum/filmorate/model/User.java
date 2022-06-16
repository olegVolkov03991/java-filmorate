package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long id;
    @NotBlank
    @NonNull
    @Size(min = 1, max = 200)
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday){
        this.email = email;
        this.login = login;
        if(name == null || name.equals("")){
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }
}
