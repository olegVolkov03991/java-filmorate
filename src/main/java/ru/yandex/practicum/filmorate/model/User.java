package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    private long id;
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String login;
    @PastOrPresent
    private LocalDate birthday;
}
