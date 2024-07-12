package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
@Primary
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createNewUser(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());

        // Получение сгенерированного id
        Long id = jdbcTemplate.queryForObject("SELECT MAX(id) FROM users", Long.class);
        user.setId(id);
        log.info("Пользователю присвоен id = {}", user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT id, email, login, name, birthday, friend_status FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            long id = rs.getLong("id");
            String email = rs.getString("email");
            String login = rs.getString("login");
            String name = rs.getString("name");
            LocalDate birthday = rs.getDate("birthday").toLocalDate();
            boolean friendStatus = rs.getBoolean("friend_status");
            log.info("Получен список пользователей");

            return new User(id, email, login, name, birthday, friendStatus);
        });
    }

    @Override
    public User updateUser(User user) {
        long id = user.getId();
        getUserId(id);
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        log.info("Пользователь успешно обновлен");
        return user;
    }

    @Override
    public User getUserId(Long id) {
        String sql = "SELECT id, email, login, name, birthday, friend_status FROM users WHERE id = ?";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate(),
                rs.getBoolean("friend_status")
        ), id);
        log.info("Получен список пользователей");
        if (users.isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Пользователь получен по id = {}", id);
        return users.get(0);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        if (getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден.");
        } else if (getUserId(friendId) == null) {
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

    @Override
    public void deleteFriend(long userId, long friendId) {
        if (getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден.");
        } else if (getUserId(friendId) == null) {
            throw new NotFoundException("Друг с таким id не найден.");
        } else {
            String sql = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";
            jdbcTemplate.update(sql, userId, friendId);
            User user = getUserId(userId);
            user.setFriendStatus(false);
        }
    }

    @Override
    public List<User> getAllFriends(long userId) {
        if (getUserId(userId) == null) {
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

    @Override
    public List<User> findCommonFriends(long userId, long anotherUserId) {
        if (getUserId(userId) == null) {
            throw new NotFoundException("Пользователь с таким id не найден.");
        } else if (getUserId(anotherUserId) == null) {
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

    private boolean isFriendship(long userId, long friendId) {
        String sql = "SELECT COUNT(*) FROM user_friends WHERE user_id = ? AND friend_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{userId, friendId}, Integer.class);
        return count > 0;
    }
}
