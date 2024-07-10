package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreService {

    private final JdbcTemplate jdbcTemplate;

    public Genre getGenreId(long id) {
        List<Genre> genres = jdbcTemplate.query("SELECT * FROM genres WHERE id = ?", genreRowMapper(), id);

        if (genres.isEmpty()) {
            throw new NotFoundException("Жанр с id " + id + " не найден");
        }

        return genres.get(0);
    }

    public List<Genre> getAllGenres() {
        List<Genre> genres = jdbcTemplate.query("SELECT * FROM genres", genreRowMapper());

        if (genres.isEmpty()) {
            throw new NotFoundException("Такого жанра нет в базе данных");
        }

        return genres;
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(
                rs.getLong("id"),
                rs.getString("name")
        );
    }
}
