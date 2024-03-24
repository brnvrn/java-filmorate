package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
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
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    protected int generatorId = 0;

    @PostMapping("/new/")
    public int addNewUser(@RequestBody User user) {
        log.info("Добавление нового пользователя: {}", user);
        if (isValidateUser(user)) {
            int id = ++generatorId;
            user.setId(id);
            users.put(user.getId(), user);
        }
        return user.getId();
    }

    @GetMapping("/all/")
    public ArrayList<User> getAllUsers() {
        log.info("Получение всех пользователей: []");
        return new ArrayList<>(users.values());
    }

    @PutMapping("/update/{id}/")
    public User updateUser(@RequestBody User user) {
        log.info("Обновление пользователя: {}", user);
        if (isValidateUser(user)) {
            int id = user.getId();
            if (users.containsKey(id)) {
                users.remove(user.getId());
                users.put(user.getId(), user);
            }
        }
        return user;
    }

    private boolean isValidateUser(User user) throws ValidationException {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            System.out.println("Email не должен быть пустым и должен содержать символ (@)");
            return false;
        }

        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            System.out.println("Логин не должен быть пустым и содержать пробелы");
            return false;
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            System.out.println("Имя не может быть пустым");
            return false;
        }

        LocalDate currentDate = LocalDate.now();
        if (user.getBirthday().isAfter(currentDate)) {
            System.out.println("Дата не должна быть в будущем");
            return false;
        }

        return true;
    }
}
