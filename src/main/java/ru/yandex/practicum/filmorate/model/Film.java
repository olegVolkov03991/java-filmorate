package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Mpa mpa;
    private Set<Genres> genres = new TreeSet<>(Comparator.comparingInt(Genres::getId));

    public Film(int id, String name, Mpa mpa, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.mpa = mpa;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("mpa_id", mpa.getId());
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        return values;
    }
}