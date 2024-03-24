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
@RequestMapping("/films/")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    protected int generatorId = 0;

    @PostMapping("/new/")
    public int addNewFilm(@RequestBody Film film) {
        log.info("Добавление нового фильма: {}", film);
        if (isValidateFilm(film)) {
            int id = ++generatorId;
            film.setId(id);
            films.put(film.getId(), film);
        }
        return film.getId();
    }

    @GetMapping("/all/")
    public ArrayList<Film> getAllFilms() {
        log.info("Получение всех фильмов: []");
        return new ArrayList<>(films.values());
    }

    @PutMapping("/update/{id}/")
    public Film updateFilm(@RequestBody Film film) {
        log.info("Обновление фильма: {}", film);
        if (isValidateFilm(film)) {
            int id = film.getId();
            if (films.containsKey(id)) {
                films.remove(film.getId());
                films.put(film.getId(), film);
            }
        }
        return film;
    }

    private boolean isValidateFilm(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isEmpty()) {
            System.out.println("Имя не должно быть пустым");
            return false;
        }

        if (film.getDescription() == null || film.getDescription().isEmpty() || film.getDescription().length() > 200) {
            System.out.println("Описание не должно быть пустым и превышать 200 символов");
            return false;
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            System.out.println("Дата не должна быть раньше даты выхода кино (28.12.1895г.)");
            return false;
        }

        if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            System.out.println("Длительность фильма не должна быть отрицательной или равно нулю");
            return false;
        }
        return true;
    }
}

