package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.*;

@Data
public class Film {
    private Long id;
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Long> likes = new HashSet<>();
    private Mpa mpa;
    private List<Genre> genres = new ArrayList<>();

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("film_name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        if (mpa != null)
            values.put("mpa_id", getMpa().getId());
        return values;
    }
}
