package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createNewUser(User user);

    List<User> getAllUsers();

    User updateUser(User user);

    User getUserId(Long id);
    void addFriend(long userId, long friendId);
    void deleteFriend(long userId, long friendId);
    List<User> getAllFriends(long userId);
    List<User> findCommonFriends(long userId, long anotherUserId);
}
