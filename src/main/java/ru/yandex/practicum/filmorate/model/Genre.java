package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Data
@ToString
@AllArgsConstructor
public class Genre {
    @Max(6)
    private long id;
    @NotBlank(message = "Имя не должно быть пустым")
    private String name;
}
