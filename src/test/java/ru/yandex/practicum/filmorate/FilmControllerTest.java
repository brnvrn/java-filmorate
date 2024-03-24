package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmControllerTest {
    private FilmController filmController;
    private final Film film1 = new Film(1, "Lola", "Comedy",
            LocalDate.of(2003, 3, 26), Duration.ofMinutes(80));

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAddNewFilm() {

        int id = filmController.addNewFilm(film1);

        assertTrue(id > 0);
        assertEquals(film1, filmController.getAllFilms().get(0));
    }

    @Test
    void testAddNewFilWithNullName() {
        Film film2 = new Film(1, null, "Comedy", LocalDate.of(2003, 3, 26),
                Duration.ofMinutes(80));

        filmController.addNewFilm(film2);

        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    void testAddNewFilWithEmptyName() {
        Film film2 = new Film(1, "", "Comedy", LocalDate.of(2003, 3, 26),
                Duration.ofMinutes(80));

        filmController.addNewFilm(film2);

        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    void testAddNewFilWithNullDescription() {
        Film film2 = new Film(1, "Lola", null, LocalDate.of(2003, 3, 26),
                Duration.ofMinutes(80));

        filmController.addNewFilm(film2);

        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    void testAddNewFilWithLongDescription() {
        Film film2 = new Film(1, "Lola", "«Лола» (нем. Lola) — художественный фильм режиссёра " +
                "Райнера Вернера Фассбиндера, вышедший на экраны в 1981 году. Фильм является третьей частью — ФРГ 3 —" +
                " задуманного цикла об истории Западной Германии[2][3]. Посвящён Александру Клюге. Лента получила приз " +
                "«Туфля Чаплина» за лучшую женскую роль (Барбара Зукова) на Мюнхенском кинофестивале, а также три" +
                " премии Deutscher Filmpreis: серебряная премия за лучший фильм, золотые премии за лучшую женскую " +
                "(Барбара Зукова) и мужскую роли (Армин Мюллер-Шталь).", LocalDate.of(2003, 3, 26),
                Duration.ofMinutes(80));

        filmController.addNewFilm(film2);

        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    void testAddNewFilWithBeforeReleaseDate() {
        Film film2 = new Film(1, "Lola", "Comedy", LocalDate.of(1894, 3, 26),
                Duration.ofMinutes(80));

        filmController.addNewFilm(film2);

        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    void testAddNewFilWithNegativeDuration() {
        Film film2 = new Film(1, "Lola", "Comedy", LocalDate.of(2003, 3, 26),
                Duration.ofMinutes(-80));

        filmController.addNewFilm(film2);

        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    void testAddNewFilWithZeroDuration() {
        Film film2 = new Film(1, "Lola", "Comedy", LocalDate.of(2003, 3, 26),
                Duration.ofMinutes(0));

        filmController.addNewFilm(film2);

        assertTrue(filmController.getAllFilms().isEmpty());
    }

    @Test
    void testGetAllFilms() {
        Film film2 = new Film(2, "Lola", "Comedy", LocalDate.of(2003, 3, 26),
                Duration.ofMinutes(90));

        filmController.addNewFilm(film1);
        filmController.addNewFilm(film2);

        assertEquals(2, filmController.getAllFilms().size());
        assertTrue(filmController.getAllFilms().contains(film1));
        assertTrue(filmController.getAllFilms().contains(film2));
    }

    @Test
    void testGetAllFilmsWithNullName() {
        Film film2 = new Film(2, null, "Comedy", LocalDate.of(2003, 3, 26),
                Duration.ofMinutes(90));

        filmController.addNewFilm(film2);
        filmController.getAllFilms();

        assertEquals(0, filmController.getAllFilms().size());
        assertFalse(filmController.getAllFilms().contains(film2));
    }

    @Test
    void testGetAllFilmsWithEmptyName() {
        Film film2 = new Film(2, "", "Comedy", LocalDate.of(2003, 3, 26),
                Duration.ofMinutes(90));

        filmController.addNewFilm(film2);
        filmController.getAllFilms();

        assertEquals(0, filmController.getAllFilms().size());
        assertFalse(filmController.getAllFilms().contains(film2));
    }

    @Test
    void testGetAllFilmsWithNullDescription() {
        Film film2 = new Film(2, "Lola", "", LocalDate.of(2003, 3, 26),
                Duration.ofMinutes(90));

        filmController.addNewFilm(film2);
        filmController.getAllFilms();

        assertEquals(0, filmController.getAllFilms().size());
        assertFalse(filmController.getAllFilms().contains(film2));
    }

    @Test
    void testGetAllFilmWithLongDescription() {
        Film film2 = new Film(1, "Titanic", "Титаник - американский романтический " +
                "фильм-катастрофа 1997 года, снятый режиссером, сценаристом, продюсером и соредактором Джеймсом " +
                "Кэмероном. Включающий как исторические, так и беллетризованные аспекты, он основан на рассказах о " +
                "затоплении RMS \"Титаник\" в 1912 году. Кейт Уинслет и Леонардо Ди Каприо снимаются в роли " +
                "представителей разных социальных слоев, которые влюбляются друг в друга во время первого рейса " +
                "корабля. ", LocalDate.of(1997, 12, 19), Duration.ofMinutes(210));

        filmController.addNewFilm(film2);
        filmController.getAllFilms();

        assertEquals(0, filmController.getAllFilms().size());
        assertFalse(filmController.getAllFilms().contains(film2));
    }

    @Test
    void testGetAllFilmWithBeforeReleaseDate() {
        Film film2 = new Film(2, "Lola", "Drama", LocalDate.of(1818, 3, 26),
                Duration.ofMinutes(90));

        filmController.addNewFilm(film2);
        filmController.getAllFilms();

        assertEquals(0, filmController.getAllFilms().size());
        assertFalse(filmController.getAllFilms().contains(film2));
    }

    @Test
    void testGetAllFilmWithNegativeDuration() {
        Film film2 = new Film(2, "Lola", "Drama", LocalDate.of(2003, 3, 26),
                Duration.ofMinutes(-1));

        filmController.addNewFilm(film2);
        filmController.getAllFilms();

        assertEquals(0, filmController.getAllFilms().size());
        assertFalse(filmController.getAllFilms().contains(film2));
    }

    @Test
    void testGetAllFilmWithZeroDuration() {
        Film film2 = new Film(2, "Lola", "Drama", LocalDate.of(2003, 3, 26),
                Duration.ofMinutes(0));

        filmController.addNewFilm(film2);
        filmController.getAllFilms();

        assertEquals(0, filmController.getAllFilms().size());
        assertFalse(filmController.getAllFilms().contains(film2));
    }

    @Test
    void testUpdateFilm() {
        Film film = new Film(1, "Titanic", "Drama",
                LocalDate.of(1997, 12, 19), Duration.ofMinutes(195));

        filmController.addNewFilm(film);

        Film updatedFilm = new Film(1, "Titanic: Special Edition", "Drama",
                LocalDate.of(1997, 12, 19), Duration.ofMinutes(210));
        Film result = filmController.updateFilm(updatedFilm);

        assertEquals(updatedFilm, result);
    }

    @Test
    void testUpdateFilmWithNullName() {
        Film film = new Film(1, "Titanic", "Drama",
                LocalDate.of(1997, 12, 19), Duration.ofMinutes(195));

        filmController.addNewFilm(film);

        Film updatedFilm = new Film(1, null, "Drama",
                LocalDate.of(1997, 12, 19), Duration.ofMinutes(210));
        filmController.updateFilm(updatedFilm);

        assertFalse(filmController.getAllFilms().contains(updatedFilm));
    }

    @Test
    void testUpdateFilmWithEmptyName() {
        Film film = new Film(1, "Titanic", "Drama",
                LocalDate.of(1997, 12, 19), Duration.ofMinutes(195));

        filmController.addNewFilm(film);

        Film updatedFilm = new Film(1, "", "Drama",
                LocalDate.of(1997, 12, 19), Duration.ofMinutes(210));
        filmController.updateFilm(updatedFilm);

        assertFalse(filmController.getAllFilms().contains(updatedFilm));
    }

    @Test
    void testUpdateFilmWithNullDescription() {
        Film film = new Film(1, "Titanic", "Drama",
                LocalDate.of(1997, 12, 19), Duration.ofMinutes(195));

        filmController.addNewFilm(film);

        Film updatedFilm = new Film(1, "Titanic", null,
                LocalDate.of(1997, 12, 19), Duration.ofMinutes(210));
        filmController.updateFilm(updatedFilm);

        assertFalse(filmController.getAllFilms().contains(updatedFilm));
    }

    @Test
    void testUpdateFilmWithLongDescription() {
        Film film = new Film(1, "Titanic", "Drama",
                LocalDate.of(1997, 12, 19), Duration.ofMinutes(195));

        filmController.addNewFilm(film);

        Film updatedFilm = new Film(1, "Titanic", "Титаник - американский романтический " +
                "фильм-катастрофа 1997 года, снятый режиссером, сценаристом, продюсером и соредактором Джеймсом " +
                "Кэмероном. Включающий как исторические, так и беллетризованные аспекты, он основан на рассказах о " +
                "затоплении RMS \"Титаник\" в 1912 году. Кейт Уинслет и Леонардо Ди Каприо снимаются в роли " +
                "представителей разных социальных слоев, которые влюбляются друг в друга во время первого рейса " +
                "корабля. ", LocalDate.of(1997, 12, 19), Duration.ofMinutes(210));

        filmController.updateFilm(updatedFilm);

        assertFalse(filmController.getAllFilms().contains(updatedFilm));
    }

    @Test
    void testUpdateFilmWithBeforeReleaseDate() {
        Film film = new Film(1, "Titanic", "Drama",
                LocalDate.of(1997, 12, 19), Duration.ofMinutes(195));

        filmController.addNewFilm(film);

        Film updatedFilm = new Film(1, "Titanic", "Drama",
                LocalDate.of(1818, 12, 19), Duration.ofMinutes(210));
        filmController.updateFilm(updatedFilm);

        assertFalse(filmController.getAllFilms().contains(updatedFilm));
    }

    @Test
    void testUpdateFilmWithZeroDuration() {
        Film film = new Film(1, "Titanic", "Drama",
                LocalDate.of(1997, 12, 19), Duration.ofMinutes(195));

        filmController.addNewFilm(film);

        Film updatedFilm = new Film(1, "Titanic", "Drama",
                LocalDate.of(1997, 12, 19), Duration.ofMinutes(0));
        filmController.updateFilm(updatedFilm);

        assertFalse(filmController.getAllFilms().contains(updatedFilm));
    }

    @Test
    void testUpdateFilmWithNegativeDuration() {
        Film film = new Film(1, "Titanic", "Drama",
                LocalDate.of(1997, 12, 19), Duration.ofMinutes(195));

        filmController.addNewFilm(film);

        Film updatedFilm = new Film(1, "Titanic", "Drama",
                LocalDate.of(1997, 12, 19), Duration.ofMinutes(-19));
        filmController.updateFilm(updatedFilm);

        assertFalse(filmController.getAllFilms().contains(updatedFilm));
    }
}

