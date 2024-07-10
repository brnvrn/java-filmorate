package ru.yandex.practicum.filmorate.storage;

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
}
