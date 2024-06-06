package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@ToString
@AllArgsConstructor
public class User {
    private long id;
    @NotBlank(message = "Электронная почта не должна быть пустой")
    @Email(message = "Почта не содержит @ или написана неправильно")
    private String email;

    @NotNull(message = "Логин не должен быть пустым")
    @NotBlank(message = "Логин не должен содержать пробелы")
    private String login;

    private String name;

    @NotNull(message = "Дата рождения не может быть пустой")
    @Past(message = "Дата рождения не должна быть в будущем")
    private LocalDate birthday;

    private final Set<Long> friends = new HashSet<>();
}

