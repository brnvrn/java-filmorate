package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class Mpa {
    @NotNull
    private Long id;
    @NotBlank(message = "Имя не должно быть пустым")
    private String name;
}
