package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@RequiredArgsConstructor
@Repository
@Primary
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpaId(Long id) {
        List<Mpa> mpas = jdbcTemplate.query("SELECT * FROM mpa_ratings WHERE id = ?", mpaRowMapper(), id);
        if (mpas.isEmpty()) {
            throw new NotFoundException("Mpa с id " + id + " нет в базе данных");
        }

        return mpas.get(0);
    }

    @Override
    public List<Mpa> getAllMpa() {
        List<Mpa> mpaList = jdbcTemplate.query("SELECT * FROM mpa_ratings", mpaRowMapper());

        if (mpaList.isEmpty()) {
            throw new NotFoundException("В базе данных нет записей MPA");
        }

        return mpaList;
    }

    private RowMapper<Mpa> mpaRowMapper() {
        return (rs, rowNum) -> new Mpa(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}
