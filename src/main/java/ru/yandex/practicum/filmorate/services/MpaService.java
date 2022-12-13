package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MpaService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Collection<Mpa> getAllMpa() {
        List<Mpa> res = new ArrayList<>();
        String sqlQuery = "SELECT * FROM mpa";
        List<Map<String, Object>> allMpas = jdbcTemplate.queryForList(sqlQuery);
        for (Map<String, Object> elem : allMpas) {
            Mpa mpa = new Mpa();
            mpa.setId((Integer) elem.get("mpa_id"));
            mpa.setName((String) elem.get("name"));
            res.add(mpa);
        }
        return res;
    }

    public Mpa getMpaById(long id) {
        if (id < 1 || id > 5)
            throw new ValidationException("Invalid Mpa id");
        Mpa res = new Mpa();
        String sqlQuery = "SELECT name FROM mpa WHERE mpa_id = " + id;
        Map<String, Object> all = jdbcTemplate.queryForMap(sqlQuery);
        res.setName((String) all.get("name"));
        res.setId((int) id);
        return res;
    }
}
