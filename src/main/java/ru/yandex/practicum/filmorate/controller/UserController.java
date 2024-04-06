package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    protected int generatorId = 0;

    @PostMapping
    public User addNewUser(@RequestBody User user) {
        log.info("Добавление нового пользователя: {}", user);
        if (users.containsKey(user.getId())) {
            log.info("Пользователь с таким id уже существует");
            throw new ValidationException("Пользователь с таким id уже существует");
        }
        validateUser(user);
        int id = ++generatorId;
        user.setId(id);
        users.put(user.getId(), user);

        return user;
    }


    @GetMapping
    public ArrayList<User> getAllUsers() {
        log.info("Получение всех пользователей: []");
        return new ArrayList<>(users.values());
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Обновление пользователя: {}", user);
        validateUser(user);
        int id = user.getId();
        if (users.containsKey(id)) {
            users.replace(user.getId(), user);
        } else {
            throw new ValidationException("Такого пользователя нет");
        }

        return user;
    }


    private void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Email не должен быть пустым и должен содержать символ (@)");
        }

        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен быть пустым и содержать пробелы");
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.info("Поле имя заполнено логином");
        }

        LocalDate currentDate = LocalDate.now();
        if (user.getBirthday() == null || user.getBirthday().isAfter(currentDate)) {
            throw new ValidationException("Дата не должна быть пустой или быть в будущем");
        }
    }
}