package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GenreService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Genre getGenreById(int id) {
        if (id < 1 || id > 6)
            throw new ValidationException("Invalid genre id");
        Map<String, Object> raw = jdbcTemplate.queryForMap("SELECT name FROM genreBase WHERE genre_id = " + id);
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(raw.get("name").toString());
        return genre;
    }

    public Collection<Genre> getAllGenres() {
        List<Map<String, Object>> raw = jdbcTemplate.queryForList("SELECT * FROM genreBase");
        List<Genre> all = new ArrayList<>();
        for (Map<String, Object> elem : raw) {
            Genre genre = new Genre();
            genre.setId((Integer) elem.get("genre_id"));
            genre.setName((String) elem.get("name"));
            all.add(genre);
        }
        return all;
    }
}
