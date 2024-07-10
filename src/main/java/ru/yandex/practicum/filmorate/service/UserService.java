package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public User createNewUser(User user) {
        return userStorage.createNewUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserId(Long id) {
        return userStorage.getUserId(id);
    }

    public void addFriend(long userId, long friendId) {
        if (userStorage.getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден.");
        } else if (userStorage.getUserId(friendId) == null) {
            throw new NotFoundException("Друг с таким id не найден.");
        } else {
            User user = getUserId(userId);
            user.getFriends().add(friendId);

            if (!isFriendship(userId, friendId)) {
                String sql = "INSERT INTO user_friends (user_id, friend_id) VALUES (?, ?)";
                jdbcTemplate.update(sql, userId, friendId);
                user.setFriendStatus(true);

                log.info("Пользователь с id = {} теперь у Вас в друзьях {}", userId, friendId);
            } else {
                log.info("Пользователь с id = {} уже является другом пользователя с id = {}", friendId, userId);
            }
        }
    }

    private boolean isFriendship(long userId, long friendId) {
        String sql = "SELECT COUNT(*) FROM user_friends WHERE user_id = ? AND friend_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{userId, friendId}, Integer.class);
        return count > 0;
    }

    public void deleteFriend(long userId, long friendId) {
        if (userStorage.getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден.");
        } else if (userStorage.getUserId(friendId) == null) {
            throw new NotFoundException("Друг с таким id не найден.");
        } else {
            String sql = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";
            jdbcTemplate.update(sql, userId, friendId);
            User user = getUserId(userId);
            user.setFriendStatus(false);
        }
    }

    public List<User> getAllFriends(long userId) {
        if (userStorage.getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден.");
        } else {
            String sql = "SELECT id, email, login, name, birthday, friend_status FROM users WHERE id IN " +
                    "(SELECT friend_id FROM  user_friends WHERE user_id = ?)";
            return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getString("name"),
                    rs.getDate("birthday").toLocalDate(),
                    rs.getBoolean("friend_status")
            ), userId);
        }
    }

    public List<User> findCommonFriends(long userId, long anotherUserId) {
        if (userStorage.getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден.");
        } else if (userStorage.getUserId(anotherUserId) == null) {
            throw new NotFoundException("Друг с таким id не найден.");
        } else {
            String sql = "SELECT id, email, login, name, birthday, friend_status " +
                    "FROM users " +
                    "WHERE id IN (SELECT friend_id FROM  user_friends WHERE user_id = ?) " +
                    "AND id IN (SELECT friend_id FROM  user_friends WHERE user_id = ?)";
            return jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getString("name"),
                    rs.getDate("birthday").toLocalDate(),
                    rs.getBoolean("friend_status")
            ), userId, anotherUserId);
        }
    }

    private List<Long> getAllFriendsForUser(long userId) {
        if (userStorage.getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден.");
        } else {
            String sql = "SELECT friend_id FROM  user_friends WHERE user_id = ?";
            return jdbcTemplate.queryForList(sql, Long.class, userId);
        }
    }
}