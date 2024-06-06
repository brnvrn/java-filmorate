package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void setLike(long filmId, long userId) {
        if (filmStorage.getFilmId(filmId) == null) {
            throw new NotFoundException("Фильм с таким id не найден");
        } else if (userStorage.getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден");
        } else {
            Film filmWithLike = filmStorage.getFilmId(filmId);
            filmWithLike.getUsersLikes().add(userId);
            filmWithLike.setLike(filmWithLike.getLike() + 1);
            log.info("Фильму поставлен лайк");
        }
    }

    public void deleteLike(long filmId, long userId) {
        if (filmStorage.getFilmId(filmId) == null) {
            throw new NotFoundException("Фильм с таким id не найден");
        } else if (userStorage.getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден");
        } else {
            Film filmWithLike = filmStorage.getFilmId(filmId);
            filmWithLike.getUsersLikes().remove(userId);
            filmWithLike.setLike(filmWithLike.getLike() - 1);
            log.info("Лайк убран");
        }
    }

    public List<Film> getTopLikedFilms(int count) {
        log.info("Получение списка популярных фильмов");
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparing(Film::getLike).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
