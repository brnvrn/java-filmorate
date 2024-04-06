package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final LocalDate cinemaBirthday = LocalDate.of(1895, 12, 28);

    private final Map<Integer, Film> films = new HashMap<>();

    protected int generatorId = 0;

    @PostMapping
    public Film addNewFilm(@RequestBody Film film) throws ValidationException {
        log.info("Добавление нового фильма: {}", film);
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с таким id уже существует");
        }
        validateFilm(film);
        int id = ++generatorId;
        film.setId(id);
        films.put(film.getId(), film);

        return film;
    }


    @GetMapping
    public ArrayList<Film> getAllFilms() {
        log.info("Получение всех фильмов: []");
        return new ArrayList<>(films.values());
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        log.info("Обновление фильма: {}", film);
        validateFilm(film);
        int id = film.getId();
        if (films.containsKey(id)) {
            films.remove(film.getId());
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Такого фильма нет");
        }

        return film;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Имя не должно быть пустым");
        }

        if (film.getDescription() == null || film.getDescription().isEmpty() || film.getDescription().length() > 200) {
            throw new ValidationException("Описание не должно быть пустым и превышать 200 символов");
        }

        if (film.getReleaseDate().isBefore(cinemaBirthday)) {
            throw new ValidationException("Дата не должна быть раньше даты выхода кино (28.12.1895г.)");
        }

        if (film.getDuration() <= 0) {
            throw new ValidationException("Длительность фильма не должна быть отрицательной или равно нулю");
        }
    }
}