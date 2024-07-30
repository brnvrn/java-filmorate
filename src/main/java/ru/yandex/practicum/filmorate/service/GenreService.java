package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreService {

    private final GenreStorage genreStorage;

    public Genre getGenreId(long id) {
        return genreStorage.getGenreId(id);
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }
}
