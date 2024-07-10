package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createNewFilm(Film film);

    List<Film> getAllFilms();

    Film updateFilm(Film film);

    Film getFilmId(long filmId);

    List<Film> getPopularFilms(int count);
}
