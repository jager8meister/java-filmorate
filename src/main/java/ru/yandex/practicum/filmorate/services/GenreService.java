package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public class GenreService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getGenreById(int id) {
        if (id < 1 || id > 6)
            throw new ValidationException("Invalid genre id");
        Map<String, Object> raw = jdbcTemplate.queryForMap("SELECT name FROM genres WHERE genre_id = " + id);
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(raw.get("name").toString());
        return genre;
    }

    public Collection<Genre> getAllGenres() {
        List<Map<String, Object>> raw = jdbcTemplate.queryForList("SELECT * FROM genres");
        return FilmDbStorage.getAllFilmGenres(raw);
    }
}
