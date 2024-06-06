package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(long userId, long friendId) {
        if (userStorage.getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден.");
        } else if (userStorage.getUserId(friendId) == null) {
            throw new NotFoundException("Друг с таким id не найден.");
        } else {
            User user = userStorage.getUserId(userId);
            User friend = userStorage.getUserId(friendId);
            user.getFriends().add(friendId);
            friend.getFriends().add(userId);
            log.info("Пользователь с id = {} теперь у Вас в друзьях {}", userId, friendId);
        }
    }

    public void deleteFriend(long userId, long friendId) {
        if (userStorage.getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден.");
        } else if (userStorage.getUserId(friendId) == null) {
            throw new NotFoundException("Друг с таким id не найден.");
        } else {
            User user = userStorage.getUserId(userId);
            User friend = userStorage.getUserId(friendId);
            user.getFriends().remove(friendId);
            friend.getFriends().remove(userId);
            log.info("Пользователь с id = {} удален из друзей", friendId);
        }
    }

    public List<User> getAllFriends(long userId) {
        if (userStorage.getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден.");
        } else {
            User user = userStorage.getUserId(userId);
            List<User> friends = new ArrayList<>();
            for (long friendId : user.getFriends()) {
                friends.add(userStorage.getUserId(friendId));
            }
            return friends;
        }
    }

    public List<User> findCommonFriends(long userId, long anotherUserId) {
        if (userStorage.getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден.");
        } else if (userStorage.getUserId(anotherUserId) == null) {
            throw new NotFoundException("Друг с таким id не найден.");
        } else {
            User user = userStorage.getUserId(userId);
            User anotherUser = userStorage.getUserId(anotherUserId);
            log.info("Список общих друзей");
            return user.getFriends().stream()
                    .filter(id -> anotherUser.getFriends().contains(id))
                    .map(userStorage::getUserId)
                    .collect(Collectors.toList());
        }
    }
}
