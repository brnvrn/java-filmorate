package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final String TOP_FILMS = "10";
    private final FilmService filmService;


    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addNewFilm(@RequestBody @Valid Film film) {
        log.info("Добавление нового фильма: {}", film);
        return filmService.createNewFilm(film);
    }


    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получение всех фильмов: []");
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilmId(@PathVariable Long filmId) {
        return filmService.getFilmId(filmId);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Обновление фильма: {}", film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void setLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Лайк для фильма с id = {}", id);
        filmService.setLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Удалить лайк для фильма с id = {}", id);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getTopLikedFilms(@RequestParam(defaultValue = TOP_FILMS) int count) {
        log.info("Получение всех фильмов: []");
        return filmService.getTopLikedFilms(count);
    }
}