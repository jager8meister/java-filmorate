package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class GenreService {

    private final JdbcTemplate jdbcTemplate;
    final int minimalGenreIndex = 1;
    final int maximumGenreIndex = 6;

    @Autowired
    public GenreService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getGenreById(int id) {
        if (id < minimalGenreIndex || id > maximumGenreIndex) {
            throw new ValidationException("Invalid genre id");
        }
        Map<String, Object> raw = jdbcTemplate.queryForMap("SELECT name FROM genres WHERE genre_id = " + id);
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(raw.get("name").toString());
        return genre;
    }

    public Collection<Genre> getAllGenres() {
        return getAllFilmGenres(jdbcTemplate.queryForList("SELECT * FROM genres"));
    }

    public static List<Genre> getAllFilmGenres(List<Map<String, Object>> raw) {
        List<Genre> allFilmGenres = new ArrayList<>();
        for (Map<String, Object> genreElem : raw) {
            Genre genre = new Genre();
            genre.setId((Integer) genreElem.get("genre_id"));
            genre.setName((String) genreElem.get("name"));
            allFilmGenres.add(genre);
        }
        return allFilmGenres;
    }
}
