package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.Accessors;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class Film {
    private long id;
    @NotBlank
    @Size(max = 200, message = "Имя не должно быть длинее 200 символов")
    private String name;

    @NotBlank(message = "Описание не должно быть пустым")
    @Size(max = 200, message = "Описание не должно быть длинее 200 символов")
    private String description;

    @NotNull(message = "Дата не должна быть пустой")
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность не должна быть пустой")
    @Positive(message = "Дата не должна быть меньше 0")
    private int duration;
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();
    private Set<Long> userIdLikes = new HashSet<>();
    private int likes = 0;
}
