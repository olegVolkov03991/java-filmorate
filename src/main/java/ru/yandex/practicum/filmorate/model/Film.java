package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film{
    @NonNull
    private long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание не может привышать 200 символов")
    private String description;

    @NonNull
    private LocalDate releaseDate;

    @NonNull
    @Positive(message = "Продолжительность фильма не может быть отрицательной")
    private long  duration;
}
