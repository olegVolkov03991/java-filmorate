package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private long id;
    @NotBlank
    @NonNull
    @Size(min = 1, max = 200)
    @Email()
    private String email;
    @NotEmpty
    @NonNull
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;
}
