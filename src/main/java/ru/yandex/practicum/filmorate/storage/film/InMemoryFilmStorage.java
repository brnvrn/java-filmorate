package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);
    private UserStorage userStorage;

    private final Map<Long, Film> films = new HashMap<>();

    protected long generatorId = 1;


    @Override
    public Film createNewFilm(Film film) {
        if (film.getReleaseDate().isAfter(cinemaBirthday)) {
            film.setId(generatorId++);
            films.put(film.getId(), film);
            log.info("Фильм успешно добавлен. Общее количество фильмов: {} ", films.size());
            return film;
        } else {
            throw new ValidationException("Дата не должна быть раньше даты выхода кино (28.12.1895г.)");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Получение всех фильмов: []");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Обновление фильма: {}", film);
        long id = film.getId();
        if (films.containsKey(id)) {
            films.remove(film.getId());
            films.put(film.getId(), film);
            return film;
        } else {
            throw new NotFoundException("Такого фильма нет");
        }
    }

    @Override
    public Film getFilmId(long filmId) {
        if (films.containsKey(filmId)) {
            return films.get(filmId);
        } else {
            log.error("Фильм с id = {} не найден", filmId);
            throw new NotFoundException("Фильм не найден");
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        log.info("Получение списка популярных фильмов");
        return getAllFilms().stream()
                .sorted(Comparator.comparing(Film::getLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void setLike(long filmId, long userId) {
        if (getFilmId(filmId) == null) {
            throw new NotFoundException("Фильм с таким id не найден");
        } else if (userStorage.getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден");
        } else {
            Film filmWithLike = getFilmId(filmId);
            filmWithLike.getUserIdLikes().add(userId);
            filmWithLike.setLikes(filmWithLike.getLikes() + 1);
            log.info("Фильму поставлен лайк");
        }
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        if (getFilmId(filmId) == null) {
            throw new NotFoundException("Фильм с таким id не найден");
        } else if (userStorage.getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден");
        } else {
            Film filmWithLike = getFilmId(filmId);
            filmWithLike.getUserIdLikes().remove(userId);
            filmWithLike.setLikes(filmWithLike.getLikes() - 1);
            log.info("Лайк убран");
        }
    }
}
