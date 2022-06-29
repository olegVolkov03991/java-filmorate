package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Film{
    private long id;
    @NotBlank
    @NonNull
    private String name;
    @Length(max = 200)
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    @Positive(message = "Movie duration cannot be negative")
    private int duration;
    private Set<Long> likes = new HashSet<>();

    public Set<Long> getLikes() {
        return likes;
    }

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
