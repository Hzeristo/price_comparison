package com.example.price_comparison.service;

import com.example.price_comparison.model.*;
import com.example.price_comparison.repository.UserRepository;
import com.example.price_comparison.exception.custom.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@Transactional
@Rollback 
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createUserSuccess() {
        User user = new User(0, "john_doe", "password123", "john@example.com", "1234567890", null, null, null);
        when(userRepository.save(user)).thenReturn(user);
        
        User createdUser = userService.createUser(user);
        Assertions.assertNotNull(createdUser);
        Assertions.assertEquals("john_doe", createdUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void createUserDuplicateUsername() {
        User user = new User(0, "john_dwqe", "password123", "john@example.com", "1234567890", null, null, null);
        userService.createUser(user); // First call to create the user
        when(userRepository.existsByUsername("john_dwqe")).thenReturn(true);
        Assertions.assertThrows(DuplicateResourceException.class, () -> userService.createUser(user));
    }

    @Test
    public void getUserByUsernameSuccess() {
        User user = new User(0, "john_doe", "password123", "john@example.com", "1234567890", null, null, null);
        when(userRepository.findByUsername("john_doe")).thenReturn(user);
        
        User foundUser = userService.getUserByUsername("john_doe");
        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals("john_doe", foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("john_doe");
    }

    @Test
    public void getUserByUsernameNotFound() {
        when(userRepository.findByUsername("unknown_user")).thenReturn(null);
        
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("unknown_user"));
    }

    @Test
    public void updateUserSuccess() {
        User existingUser = new User(0, "john_doe", "password123", "john@example.com", "1234567890", null, null, null);
        User updatedInfo = new User(0, "john_doe", "newPassword", "john.new@example.com", "0987654321", null, null, null);
        when(userRepository.findByUsername("john_doe")).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(existingUser);
        
        User updatedUser = userService.updateUser("john_doe", updatedInfo);
        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("john.new@example.com", updatedUser.getEmail());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void updateUserNotFound() {
        User updatedInfo = new User(0, "unknown_user", "newPassword", "unknown@example.com", "0987654321", null, null, null);
        when(userRepository.findByUsername("unknown_user")).thenReturn(null);
        
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUser("unknown_user", updatedInfo));
    }

    @Test
    public void deleteUserSuccess() {
        User user = new User(0, "john_doe", "password123", "john@example.com", "1234567890", null, null, null);
        when(userRepository.findByUsername("john_doe")).thenReturn(user);
        
        userService.deleteUser("john_doe");
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void deleteUserNotFound() {
        when(userRepository.findByUsername("unknown_user")).thenReturn(null);
        
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUser("unknown_user"));
    }

    @Test
    public void userExistsByUsername() {
        when(userRepository.existsByUsername("john_doe")).thenReturn(true);
        
        boolean exists = userService.userExistsByUsername("john_doe");
        Assertions.assertTrue(exists);
        verify(userRepository, times(1)).existsByUsername("john_doe");
    }

    @Test
    public void userExistsByEmail() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);
        
        boolean exists = userService.userExistsByEmail("john@example.com");
        Assertions.assertTrue(exists);
        verify(userRepository, times(1)).existsByEmail("john@example.com");
    }

    @Test
    public void userExistsByPhone() {
        when(userRepository.existsByPhone("1234567890")).thenReturn(true);
        
        boolean exists = userService.userExistsByPhone("1234567890");
        Assertions.assertTrue(exists);
        verify(userRepository, times(1)).existsByPhone("1234567890");
    }

    @Test
    public void manageAdminSuccess() {
        User user = new User(0, "john_doe", "password123", "john@example.com", "1234567890", Role.USER, null, null);
        when(userRepository.findByUsername("john_doe")).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        userService.manageAdmin("john_doe", true);
        Assertions.assertTrue(user.getRole().isAdmin());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void manageAdminAlreadyHasRole() {
        User user = new User(0, "john_doe", "password123", "john@example.com", "1234567890", Role.ADMIN, null, null);
        when(userRepository.findByUsername("john_doe")).thenReturn(user);
        
        Assertions.assertThrows(UnnecessaryUpdateException.class, () -> userService.manageAdmin("john_doe", true));
    }

    @Test
    public void manageAdminUserNotFound() {
        when(userRepository.findByUsername("unknown_user")).thenReturn(null);
        
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.manageAdmin("unknown_user", true));
    }
}
