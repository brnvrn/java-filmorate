package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createNewFilm(Film film);

    List<Film> getAllFilms();

    Film updateFilm(Film film);

    Film getFilmId(long filmId);

    List<Film> getPopularFilms(int count);

    void setLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);
}
