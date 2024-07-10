package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    protected long generatorId = 0;

    @Override
    public User createNewUser(User user) {
        log.info("Добавление нового пользователя: {}", user);

        if (users.containsKey(user.getId())) {
            log.info("Пользователь с таким id уже существует");
            throw new ValidationException("Пользователь с таким id уже существует");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.info("Поле имя заполнено логином");
        }
        long id = ++generatorId;
        user.setId(id);
        users.put(user.getId(), user);

        return user;
    }


    @Override
    public List<User> getAllUsers() {
        log.info("Получение всех пользователей: []");
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновление пользователя: {}", user);
        long id = user.getId();
        if (users.containsKey(id)) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new NotFoundException("Такого пользователя нет");
        }
    }

    @Override
    public User getUserId(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            log.error("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь не найден");
        }
    }

}