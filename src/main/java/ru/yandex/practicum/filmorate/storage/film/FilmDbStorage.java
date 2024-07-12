package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Primary
@Component
@Validated
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private final UserStorage userStorage;

    private static final LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);

    @Override
    public Film createNewFilm(Film film) {
        if (film.getReleaseDate().isAfter(cinemaBirthday) && film.getMpa().getId() <= 5) {
            String sqlQuery = "INSERT INTO films(name, description, release_date, duration, mpa_id) " +
                    "VALUES (?, ?, ?, ?, ?)";

            if (jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId())
                    == 0) {
                log.info("Операция обновления данных фильма в БД закончилась неудачей");
            }
            SqlRowSet filmRow = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE name = ?", film.getName());
            while (filmRow.next()) {
                film.setId(filmRow.getLong("id"));
                log.info("Фильму присвоен ID = {}", film.getId());
            }
            for (Genre genre : film.getGenres()) {
                if (genre.getId() <= 6) {
                    jdbcTemplate.update("INSERT INTO films_genres(film_id, genre_id) VALUES (?, ?)",
                            film.getId(), genre.getId());
                } else {
                    throw new ValidationException("Жанр с таким id не существует");
                }
            }
            return film;
        } else {
            throw new ValidationException("Нарушены правила валидации");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuerySelect = "SELECT COUNT(*) FROM films WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sqlQuerySelect, Integer.class, film.getId());
        if (count != 0) {
            log.info("Фильма с id = {} нет в базе данных", film.getId());

            String sqlQuery = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ? WHERE id = ?";
            if (jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getId()) == 0) {
                log.info("Ошибка обновления фильма");
            }

            return film;
        } else {
            throw new NotFoundException("Такого фильма не существует");
        }
    }

    @Override
    public Film getFilmId(long filmId) {
        List<Film> films = jdbcTemplate.query("SELECT * FROM films WHERE id = ?", filmRowMapper(), filmId);
        log.info("Фильм получен по id = {}", filmId);
        if (films.isEmpty()) {
            throw new NotFoundException("Фильм не найден");
        }
        Film film = films.get(0);
        Set<Genre> genres = getGenresByFilmId(filmId);
        film.setGenres(genres);

        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = jdbcTemplate.query("SELECT * FROM films", filmRowMapper());
        log.info("Получен список фильмов, размер списка = {}", films.size());
        return films;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        List<Film> films = jdbcTemplate.query("SELECT * FROM films ORDER by likes DESC limit ?", filmRowMapper(), count);
        log.info("Получение списка популярных фильмов = {}", films.size());
        return films;
    }

    @Override
    public void setLike(long filmId, long userId) {
        if (getFilmId(filmId) == null) {
            throw new NotFoundException("Фильм с таким id не найден");
        } else if (userStorage.getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден");
        } else {
            if (jdbcTemplate.update("INSERT INTO likes (film_id, user_id) VALUES (?, ?)", filmId, userId) == 0) {
                log.info("Не получилось добавить лайк");
            }
            Film film = getFilmId(filmId);
            film.getUserIdLikes().add(userId);
            film.setLikes(film.getLikes() + 1);
            setLikeInDb(filmId, film.getLikes());
        }
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        if (getFilmId(filmId) == null) {
            throw new NotFoundException("Фильм с таким id не найден");
        } else if (userStorage.getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден");
        } else {

            if (jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? and user_id = ?", filmId, userId) == 0) {
                log.info("Не получилось удалить лайк");
            }
            Film film = getFilmId(filmId);
            film.getUserIdLikes().remove(userId);
            if (film.getLikes() >= 1) {
                film.setLikes(film.getLikes() - 1);
            }
            setLikeInDb(filmId, film.getLikes());
        }
    }

    private RowMapper<Film> filmRowMapper() {
        return ((rs, rowNum) -> {
            Film film = new Film()
                    .setId(rs.getLong("id"))
                    .setName(rs.getString("name"))
                    .setDescription(rs.getString("description"))
                    .setReleaseDate(rs.getDate("release_date").toLocalDate())
                    .setDuration(rs.getInt("duration"))
                    .setLikes(rs.getInt("likes"))
                    .setMpa(getMpaById(rs.getLong("mpa_id")));

            getGenresByFilmId(rs.getLong("id"));
            film.setGenres(getGenresByFilmId(rs.getLong("id")));

            return film;
        });
    }

    private Set<Genre> getGenresByFilmId(Long id) {
        return new HashSet<>(jdbcTemplate.query("SELECT g.id AS genre_id, g.name AS genre_name FROM genres AS g " +
                        "JOIN films_genres AS fg ON g.id = fg.genre_id WHERE fg.film_id = ? ORDER BY genre_id",
                (rs, rowNum) -> new Genre(rs.getLong("genre_id"), rs.getString("genre_name")), id));
    }

    private Mpa getMpaById(Long id) {
        if (id != null) {
            return jdbcTemplate.queryForObject("SELECT * FROM mpa_ratings WHERE id = ?", mpaRowMapper(), id);
        } else {
            throw new NotFoundException("МРА не найден");
        }
    }

    private RowMapper<Mpa> mpaRowMapper() {
        return (rs, rowNum) -> new Mpa(
                rs.getLong("id"),
                rs.getString("name")
        );
    }

    private void setLikeInDb(Long filmId, int like) {
        if (jdbcTemplate.update("UPDATE films SET likes = ? WHERE id = ?", like, filmId) == 0) {
            log.info("Лайк не добавился в базу данных");
        }
        log.info("Лайк добавлен. Фильм с id = {} количество лайков {}", filmId, like);
    }
}