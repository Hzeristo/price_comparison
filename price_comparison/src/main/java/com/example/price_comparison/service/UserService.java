package com.example.price_comparison.service;

import com.example.price_comparison.model.*;
import com.example.price_comparison.repository.UserRepository;
import com.example.price_comparison.exception.custom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.*;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new user after validating for duplicates.
     *
     * @param user The user to be created.
     * @return The created user.
     * @throws DuplicateResourceException if a user with the same username, email, or phone already exists.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional
    public User createUser(User user) {
        validateUserForDuplicates(user);
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user.
     * @return The user associated with the given username.
     * @throws UserNotFoundException if no user is found with the given username.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public User getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new UserNotFoundException("Can't find user named \"" + username + "\""));
    }

    /**
     * Updates an existing user's information.
     *
     * @param username The username of the user to be updated.
     * @param user The user object containing updated information.
     * @return The updated user.
     * @throws UserNotFoundException if no user is found with the given username.
     * @throws DuplicateResourceException if the updated information conflicts with existing users.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional
    public User updateUser(String username, User user) {
        User existingUser = getUserByUsername(username);
        validateUserForDuplicates(user);
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setRole(user.getRole());
        userRepository.save(existingUser);
        return existingUser;
    }

    /**
     * Deletes a user by their username.
     *
     * @param username The username of the user to be deleted.
     * @throws UserNotFoundException if no user is found with the given username.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteUser(String username) {
        User existingUser = getUserByUsername(username);
        userRepository.delete(existingUser);
    }

    /**
     * Checks if a user exists by their username.
     *
     * @param username The username to check.
     * @return true if the user exists, false otherwise.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks if a user exists by their email.
     *
     * @param email The email to check.
     * @return true if the user exists, false otherwise.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Checks if a user exists by their phone number.
     *
     * @param phone The phone number to check.
     * @return true if the user exists, false otherwise.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public boolean userExistsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    /**
     * Checks if a user has admin role.
     *
     * @param username The username of the user to check.
     * @return true if the user is an admin, false otherwise.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    private boolean isUserAdmin(String username) {
        User existingUser = getUserByUsername(username);
        return existingUser.getRole().isAdmin();
    }

    /**
     * Manages a user's admin role.
     *
     * @param username The username of the user.
     * @param isAdmin Whether the user should be an admin or not.
     * @throws UnnecessaryUpdateException if the user already has the desired role.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void manageAdmin(String username, boolean isAdmin) {
        User user = getUserByUsername(username);
        if (user.getRole().isAdmin() == isAdmin) {
            throw new UnnecessaryUpdateException("User already has the desired role: " + (isAdmin ? "ADMIN" : "USER"));
        }
        user.setRole(isAdmin ? Role.ADMIN : Role.USER);
        userRepository.save(user); 
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return A list of all users.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
 
    /**
     * Validates a user object for duplicate fields.
     *
     * @param user The user object to validate.
     * @throws DuplicateResourceException if any duplicate fields are found.
     */
    private void validateUserForDuplicates(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        String phone = user.getPhone();
        if (userExistsByUsername(username)) {
            throw new DuplicateResourceException("Username already exists:" + " \"" + username + "\"", "username");
        }
        if (userExistsByEmail(email)) {
            throw new DuplicateResourceException("Email already exists:" + " \"" + email + "\"", "email");
        }
        if (userExistsByPhone(phone)) {
            throw new DuplicateResourceException("Phone number already exists:" + " \"" + phone + "\"", "phone");
        }
    }
}
