package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Genre {
    private int id;
    private String name;

    public void setId(int id) {
        this.id = id;
        String[] genres = {"Комедия", "Драма", "Мультфильм", "Триллер", "Документальный", "Боевик"};
        if (id > 0)
            name = genres[id - 1];
    }
}
