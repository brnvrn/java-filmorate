package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController userController;
    private final User user2 = new User(1, "ggg@mail.com", "ggg", "Geory",
            LocalDate.of(2003, 3, 26));

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAddNewUser() {

        int id = userController.addNewUser(user2);

        assertTrue(id > 0);
        assertEquals(user2, userController.getAllUsers().get(0));
    }

    @Test
    void testAddNewUserWithNullEmail() {
        User user1 = new User(1, null, "ggg", "Geory",
                LocalDate.of(2003, 3, 26));

        userController.addNewUser(user1);

        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    void testAddNewUserWithoutAtEmail() {
        User user1 = new User(1, "gggmail.com", "ggg", "Geory",
                LocalDate.of(2003, 3, 26));

        userController.addNewUser(user1);

        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    void testAddNewUserWithNullLogin() {
        User user1 = new User(1, "ggg@mail.com", null, "Geory",
                LocalDate.of(2003, 3, 26));

        userController.addNewUser(user1);

        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    void testAddNewUserWithWightSpaceLogin() {
        User user1 = new User(1, "ggg@mail.com", "g gg", "Geory",
                LocalDate.of(2003, 3, 26));

        userController.addNewUser(user1);

        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    void testAddNewUserWithNullName() {
        User user1 = new User(1, "ggg@mail.com", "ggg", null,
                LocalDate.of(2003, 3, 26));

        userController.addNewUser(user1);

        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    void testAddNewUserWithEmptyName() {
        User user1 = new User(1, "ggg@mail.com", "ggg", "",
                LocalDate.of(2003, 3, 26));

        userController.addNewUser(user1);

        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    void testAddNewUserWithNullCurrentDate() {
        User user1 = new User(1, "ggg@mail.com", "ggg", "Georgy",
                LocalDate.of(2025, 3, 26));

        userController.addNewUser(user1);

        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User(1, "af@mail.com", "aff", "Afina",
                LocalDate.of(2004, 7, 9));

        userController.addNewUser(user1);
        userController.addNewUser(user2);

        assertEquals(2, userController.getAllUsers().size());
        assertTrue(userController.getAllUsers().contains(user1));
        assertTrue(userController.getAllUsers().contains(user2));
    }

    @Test
    void testUpdateUsers() {
        User user1 = new User(1, "af@mail.com", "aff", "Afina",
                LocalDate.of(2004, 7, 9));

        userController.addNewUser(user1);

        User updatedUser = new User(1, "af@mail.com", "afFF", "Afina",
                LocalDate.of(2004, 7, 9));
        User result = userController.updateUser(updatedUser);

        assertEquals(updatedUser, result);
    }

    @Test
    void testUpdateUsersWithNullEmail() {
        User user1 = new User(1, "af@mail.com", "aff", "Afina",
                LocalDate.of(2004, 7, 9));

        userController.addNewUser(user1);

        User updatedUser = new User(1, null, "aff", "Afina",
                LocalDate.of(2004, 7, 9));
        userController.updateUser(updatedUser);

        assertFalse(userController.getAllUsers().contains(updatedUser));
    }

    @Test
    void testUpdateUsersWithContainsAtEmail() {
        User user1 = new User(1, "af@mail.com", "aff", "Afina",
                LocalDate.of(2004, 7, 9));

        userController.addNewUser(user1);

        User updatedUser = new User(1, "afmail.com", "aff", "Afina",
                LocalDate.of(2004, 7, 9));
        userController.updateUser(updatedUser);

        assertFalse(userController.getAllUsers().contains(updatedUser));
    }

    @Test
    void testUpdateUsersWithNullLoginl() {
        User user1 = new User(1, "af@mail.com", "aff", "Afina",
                LocalDate.of(2004, 7, 9));

        userController.addNewUser(user1);

        User updatedUser = new User(1, "af@mail.com", null, "Afina",
                LocalDate.of(2004, 7, 9));
        userController.updateUser(updatedUser);

        assertFalse(userController.getAllUsers().contains(updatedUser));
    }

    @Test
    void testUpdateUsersWithWightSpaceLogin(){
        User user1 = new User(1, "af@mail.com", "aff", "Afina",
                LocalDate.of(2004, 7, 9));

        userController.addNewUser(user1);

        User updatedUser = new User(1, "af@mail.com", "a ff", "Afina",
                LocalDate.of(2004, 7, 9));
        userController.updateUser(updatedUser);

        assertFalse(userController.getAllUsers().contains(updatedUser));
    }

    @Test
    void testUpdateUsersWithNullName(){
        User user1 = new User(1, "af@mail.com", "aff", "Afina",
                LocalDate.of(2004, 7, 9));

        userController.addNewUser(user1);

        User updatedUser = new User(1, "af@mail.com", "aff", null,
                LocalDate.of(2004, 7, 9));
        userController.updateUser(updatedUser);

        assertFalse(userController.getAllUsers().contains(updatedUser));
    }

    @Test
    void testUpdateUsersWithEmptyName(){
        User user1 = new User(1, "af@mail.com", "aff", "Afina",
                LocalDate.of(2004, 7, 9));

        userController.addNewUser(user1);

        User updatedUser = new User(1, "af@mail.com", "aff", "",
                LocalDate.of(2004, 7, 9));
        userController.updateUser(updatedUser);

        assertFalse(userController.getAllUsers().contains(updatedUser));
    }

    @Test
    void testUpdateUsersWithAfterCurrentDate(){
        User user1 = new User(1, "af@mail.com", "aff", "Afina",
                LocalDate.of(2004, 7, 9));

        userController.addNewUser(user1);

        User updatedUser = new User(1, "af@mail.com", "aff", "Afina",
                LocalDate.of(2024, 7, 9));
        userController.updateUser(updatedUser);

        assertFalse(userController.getAllUsers().contains(updatedUser));
    }
}