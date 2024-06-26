package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createNewUser(User user);

    List<User> getAllUsers();

    User updateUser(User user);

    User getUserId(Long id);
}
