package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.junit.After;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film{
    @NonNull
    private int id;

    @NotEmpty
    @NonNull
    private String name;

    @NonNull
    @Size(min = 1,max = 200)
    private String description;


    @NonNull
    private LocalDate releaseDate;

    @NonNull
    @Positive(message = "Продолжительность фильма не может быть отрицательной")
    private long  duration;


    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
