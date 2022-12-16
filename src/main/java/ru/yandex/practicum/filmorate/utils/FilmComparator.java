package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Comparator;

public class FilmComparator implements Comparator<Film> {

    @Override
    public int compare(Film o1, Film o2) {
        if (o2.getLikes().size() == o1.getLikes().size()) {
            return (int) (o2.getId() - o1.getId());
        }
        return o2.getLikes().size() - o1.getLikes().size();
    }
}
