package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString
@Builder
@AllArgsConstructor
public class Film {
    private long id;
    @NotBlank(message = "Имя не должно быть пустым")
    private String name;

    @NotBlank(message = "Описание не должно быть пустым")
    @Size(max = 200, message = "Описание не должно быть длинее 200 символов")
    private String description;

    @NotNull(message = "Дата не должна быть пустой")
    private LocalDate releaseDate;

    @NotNull(message = "Продолжительность не должна быть пустой")
    @Positive(message = "Дата не должна быть меньше 0")
    private int duration;

    private int like;
    private final Set<Long> usersLikes = new HashSet<>();
}
