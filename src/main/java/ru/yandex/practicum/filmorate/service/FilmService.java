package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Film createNewFilm(Film film) {
        return filmStorage.createNewFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmId(long filmId) {
        return filmStorage.getFilmId(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public void setLike(long filmId, long userId) {
        if (filmStorage.getFilmId(filmId) == null) {
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

    public void deleteLike(long filmId, long userId) {
        if (filmStorage.getFilmId(filmId) == null) {
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

    private void setLikeInDb(Long filmId, int like) {
        if (jdbcTemplate.update("UPDATE films SET likes = ? WHERE id = ?", like, filmId) == 0) {
            log.info("Лайк не добавился в базу данных");
        }
        log.info("Лайк добавлен. Фильм с id = {} количество лайков {}", filmId, like);
    }
}