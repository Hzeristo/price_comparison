package com.example.price_comparison.repository;

import com.example.price_comparison.model.User;
import com.example.price_comparison.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // Ensure the database is clean before each test
        userRepository.deleteAll();
    }

    @Test
    public void userRegistrationTest() {
        User user = new User(0, "john_doe", "password123", "john@example.com", "1234567890", null, null, null);
        userRepository.save(user);

        // Check for duplicate username
        Assertions.assertTrue(userRepository.existsByUsername("john_doe"));

        User duplicateUserByUsername = new User(0, "john_doe", "password456", "john.doe@example.com", "0987654321", null, null, null);
        try {
            userRepository.save(duplicateUserByUsername);
            Assertions.fail("Expected exception not thrown for duplicate username");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DataIntegrityViolationException);
        }

        User duplicateUserByEmail = new User(0, "jane_doe", "password123", "john@example.com", "0987654321", null, null, null);
        try {
            userRepository.save(duplicateUserByEmail);
            Assertions.fail("Expected exception not thrown for duplicate email");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DataIntegrityViolationException);
        }

        User duplicateUserByPhone = new User(0, "jane_doe", "password123", "jane@example.com", "1234567890", null, null, null);
        try {
            userRepository.save(duplicateUserByPhone);
            Assertions.fail("Expected exception not thrown for duplicate phone");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DataIntegrityViolationException);
        }

        User anotherUser = new User(0, "jane_doe", "password123", "jane@example.com", "0987654321", null, null, null);
        userRepository.save(anotherUser);

        // Validate users are stored correctly
        List<User> actualUsers = userRepository.findAll();
        System.err.println(user);
        actualUsers.forEach(System.out::println);
        Assertions.assertEquals(2, actualUsers.size());
        Assertions.assertTrue(actualUsers.contains(user));
        Assertions.assertTrue(actualUsers.contains(anotherUser));
    }

    @Test
    public void updateUserPasswordTest() {
        User user = new User(0, "test_user", "initialPassword", "test@example.com", "1234567890", null, null, null);
        userRepository.save(user);
        
        // Update password successfully
        user.setPassword("newPassword123");
        userRepository.save(user);
        User updatedUser = userRepository.findByUsername(user.getUsername());
        Assertions.assertEquals("newPassword123", updatedUser.getPassword());

        // Attempt to update password for non-existing user
        User nonExistentUser = new User(0, "non_existent_user", "randomPassword", "random@example.com", "0987654321", null, null, null);
        Assertions.assertFalse(userRepository.existsByUsername(nonExistentUser.getUsername()));
    }

    @Test
    public void removeUserTest() {
        User user = new User(0, "to_be_deleted", "password", "delete@example.com", "1234567890", null, null, null);
        userRepository.save(user);
        
        // Remove a non-existent user
        Assertions.assertFalse(userRepository.existsByUsername("non_existent_user"));
        
        // Remove an existing user
        userRepository.delete(user);
        
        // Verify user is removed
        Assertions.assertFalse(userRepository.existsByUsername(user.getUsername()));
    }

    @Test
    public void userLoginTest() {
        User user = new User(0, "login_test", "securePassword", "login@example.com", "1234567890", null, null, null);
        userRepository.save(user);
        
        // Successful login simulation
        User loggedInUser = userRepository.findByUsername(user.getUsername());
        Assertions.assertNotNull(loggedInUser);
        Assertions.assertEquals("securePassword", loggedInUser.getPassword());
        
        // Unsuccessful login for non-existent user
        User nonExistentUser = userRepository.findByUsername("non_existent_user");
        Assertions.assertNull(nonExistentUser);
    }

    @Test
    public void crudOperationsTest() {
        // Create
        User user = new User(0, "crud_user", "password", "crud@example.com", "1234567890", null, null, null);
        userRepository.save(user);
        Assertions.assertNotNull(userRepository.findByUsername("crud_user"));

        // Read
        User retrievedUser = userRepository.findByUsername("crud_user");
        Assertions.assertEquals("crud_user", retrievedUser.getUsername());

        // Update
        retrievedUser.setPassword("newPassword");
        userRepository.save(retrievedUser);
        Assertions.assertEquals("newPassword", userRepository.findByUsername("crud_user").getPassword());

        // Delete
        userRepository.delete(retrievedUser);
        Assertions.assertNull(userRepository.findByUsername("crud_user"));
    }

    @Test
    public void exceptionHandlingTest() {
        // Attempting to register user without mandatory fields
        try {
            User invalidUser = new User(0, "", null, "invalid@example.com", null, null, null, null);
            userRepository.save(invalidUser);
            Assertions.fail("Expected exception not thrown for invalid user");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof DataIntegrityViolationException);
        }
    }
}
