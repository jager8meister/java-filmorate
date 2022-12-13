package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.StorageException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.MpaService;
import ru.yandex.practicum.filmorate.storages.util.FilmStorage;
import ru.yandex.practicum.filmorate.utils.FilmComparator;
import ru.yandex.practicum.filmorate.validators.FilmValidator;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import java.time.LocalDate;
import java.util.*;

@Component
@Qualifier("filmDbStorage ")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private void checkFilmDuplicates(Film film) {
        List<Map<String, Object>> res = this.jdbcTemplate.queryForList("SELECT * FROM filmsBase" );
        for (Map<String, Object> elem : res ) {
            if (elem.get("film_name").equals(film.getName()) &&
                elem.get("description").equals(film.getDescription()) &&
                elem.get("release_date").equals(film.getReleaseDate()) &&
                elem.get("duration").equals(film.getDuration())) {
                throw new StorageException("Film already in the base");
            }
        }
    }

    private long getId(Film film) {
        List<Map<String, Object>> res = this.jdbcTemplate.queryForList("SELECT * FROM filmsBase" );
        for (Map<String, Object> elem : res ) {
            if (elem.get("film_name").equals(film.getName().toString()) &&
                    elem.get("description").equals(film.getDescription().toString()) &&
                    elem.get("release_date").equals( java.sql.Date.valueOf(film.getReleaseDate())) &&
                    elem.get("duration").equals(film.getDuration())) {
                return Long.parseLong(elem.get("ID").toString());
            }
        }
        throw new StorageException("No such film in the base");
    }

    private void checkId(long id) {
        List<Map<String, Object>> res = this.jdbcTemplate.queryForList("SELECT * FROM filmsBase" );
        for (Map<String, Object> elem : res ) {
            if (elem.get("ID").toString().equals(String.valueOf(id))) {
                return ;
            }
        }
        throw new StorageException("No film with id " + id);
    }

    @Override
    public Film addFilm(Film film) throws StorageException {
        if (FilmValidator.valid(film)) {
            SimpleJdbcInsert insertData = new
                    SimpleJdbcInsert(jdbcTemplate).
                    withTableName("filmsBase").
                    usingColumns("FILM_NAME", "DESCRIPTION",
                            "RELEASE_DATE", "DURATION", "MPA_ID")
                    .usingGeneratedKeyColumns("id");

            checkFilmDuplicates(film);
            Map<String, Object> params = new HashMap<>();
            params.put("FILM_NAME", film.getName());
            params.put("DESCRIPTION", film.getDescription());
            params.put("RELEASE_DATE", film.getReleaseDate());
            params.put("DURATION", film.getDuration());
            if (film.getMpa() != null)
                params.put("MPA_ID", film.getMpa().getId());

            String res = insertData.executeAndReturnKey(params).toString();
            film.setId(getId(film));
            if (film.getGenres() != null) {
                for (Genre genre : film.getGenres()) {
                    jdbcTemplate.update("INSERT INTO GENRE (GENRE_ID, NAME, FILM_ID) VALUES (?, ?, ?)",
                            genre.getId(),
                            genre.getName(),
                            film.getId());
                }
            }
            return film;
        }
        throw new StorageException("Invalid film.");
    }

    void removeGenreByFilmId(long filmId) {
        jdbcTemplate.execute("DELETE FROM GENRE WHERE FILM_ID = " + filmId);
    }

    @Override
    public Film updateFilm(Film film) throws StorageException {
        if (FilmValidator.valid(film)) {
            checkId(film.getId());

            String sqlQuery = "update filmsBase set " +
                    "film_name = ?, description = ?, release_date = ?, duration = ? ";
            if (film.getMpa() != null) {
                sqlQuery += ", mpa_id = " + film.getMpa().getId();
            }
            removeGenreByFilmId(film.getId());

            sqlQuery += "where id = " + film.getId();

            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration());
            if (film.getGenres() != null) {
                for (Genre genre : film.getGenres()) {
                    jdbcTemplate.update("INSERT INTO GENRE (GENRE_ID, NAME, FILM_ID) VALUES (?, ?, ?)",
                            genre.getId(),
                            genre.getName(),
                            film.getId());
                }
            }
            return film;
        }
        throw new StorageException("Invalid film.");
    }

    @Override
    public void deleteFilm(Film film) throws StorageException {
        Film check = getFilmById(film.getId());
        if (check.getName().equals(film.getName()) &&
            check.getDescription().equals(film.getDescription()) &&
            check.getReleaseDate().equals(film.getReleaseDate()) &&
            check.getDuration().equals(film.getDuration())) {
            jdbcTemplate.queryForList("DELETE FROM filmsBase WHERE id = " + film.getId());
            jdbcTemplate.queryForList("DELETE FROM likes WHERE film_id = " + film.getId());
            removeGenreByFilmId(film.getId());
        } else {
            throw new StorageException("Invalid film");
        }
    }

    private Set<Long> getLikesIds(long id) {
        Set<Long> ids = new HashSet<>();
        List<Map<String, Object>> res = this.jdbcTemplate.queryForList("SELECT user_id FROM filmsBase WHERE film_id = " + id);
        for (Map<String, Object> elem : res) {
            Long friend = Long.parseLong(elem.get("user_id").toString());
            ids.add(friend);
        }
        return ids;
    }

    private Film getFilmFromBaseElem(Map<String, Object> elem) {
        Film film = new Film();
        film.setName(elem.get("film_name").toString());
        film.setDescription(elem.get("description").toString());
        film.setDuration(Integer.valueOf(elem.get("duration").toString()));
        film.setReleaseDate(LocalDate.parse(elem.get("release_date").toString()));
        film.setId(Long.parseLong(elem.get("id").toString()));
//        Mpa mpa = new Mpa();
//        mpa.setId((Integer) elem.get("MPA_ID"));
        MpaService service = new MpaService(jdbcTemplate);
        Mpa mpa = service.getMpaById((Integer) elem.get("MPA_ID"));
        film.setMpa(mpa);

        return film;
    }

    @Override
    public Film getFilmById(long id) {
        List<Map<String, Object>> res = this.jdbcTemplate.queryForList("SELECT * FROM filmsBase" );
        for (Map<String, Object> elem : res ) {
            if (elem.get("ID").toString().equals(String.valueOf(id))) {
                Film film = getFilmFromBaseElem(elem);
                return film;
            }
        }
        throw new StorageException("No user with id " + id);
    }

    @Override
    public List<Film> getPopularCounted(int count) {
        List<Film> all = new ArrayList<>();
        List<Map<String, Object>> raw = this.jdbcTemplate.queryForList("SELECT * FROM filmsBase" );
        for (Map<String, Object> elem : raw ) {
            Film film = getFilmFromBaseElem(elem);
            all.add(film);
        }
        all.sort(new FilmComparator());
        if (count < raw.size()) {
            Set<Film> ss = new HashSet<>();
            ss.addAll(all.subList(0, count));
            return new ArrayList<>(ss);
        } else {
            Set<Film> ss = new HashSet<>();
            ss.addAll(all.subList(0, raw.size()));
            return new ArrayList<>(ss);
        }
    }

    @Override
    public Film deleteLike(long id, long userId) {
        checkId(id);
        if (userId <= 0)
            throw new StorageException("Invalid userId");
        jdbcTemplate.execute("DELETE FROM LIKES WHERE film_id = " + id + " AND user_id = " + userId);
        return getFilmById(id);
    }

    @Override
    public Film likeFilm(long id, long userId) {
        checkId(id);
        SimpleJdbcInsert insertData = new
                SimpleJdbcInsert(jdbcTemplate).
                withTableName("likes").
                usingColumns("user_id", "film_id")
                .usingGeneratedKeyColumns("id");

        Map<String, Long> params = new HashMap<>();
        params.put("film_id", id);
        params.put("user_id", userId);
        insertData.execute(params);
        return getFilmById(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        List<Film> res = new ArrayList<>();
        List<Map<String, Object>> raw = this.jdbcTemplate.queryForList("SELECT * FROM filmsBase" );
        for (Map<String, Object> elem : raw ) {
            Film film = getFilmFromBaseElem(elem);
            res.add(film);
        }
        return res;
    }
}
