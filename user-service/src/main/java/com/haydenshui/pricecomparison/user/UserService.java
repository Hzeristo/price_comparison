package com.haydenshui.pricecomparison.user;

import com.haydenshui.pricecomparison.shared.model.*;
import com.haydenshui.pricecomparison.shared.exception.custom.*;
import com.haydenshui.pricecomparison.shared.exception.GlobalExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Creates a new user after validating for duplicates.
     *
     * @param user The user to be created.
     * @return The created user.
     * @throws DuplicateResourceException if a user with the same username, email, or phone already exists.
     */
    
    @Transactional
    public User createUser(User user) {
        validateUserForDuplicates(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user.
     * @return The user associated with the given username.
     * @throws ResourceNotFoundException if no user is found with the given username.
     */
    public User getUserByUsername(String username) {
        System.out.println("Getting user by username: " + username);
        return Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new ResourceNotFoundException("Can't find user named \"" + username + "\"", "user"));
    }

    /**
     * Updates an existing user's information except password.
     *
     * @param username The username of the user to be updated.
     * @param user The user object containing updated information.
     * @return The updated user.
     * @throws ResourceNotFoundException if no user is found with the given username.
     * @throws DuplicateResourceException if the updated information conflicts with existing users.
     */
    @Transactional
    public User updateUser(String username, Map<String, Object> updates) {
        System.out.println("Updating user: " + username);
        User existingUser = getUserByUsername(username);
        validateUserForDuplicates(updates);
        Map<String, Consumer<Object>> fieldUpdaters = new HashMap<>();
        fieldUpdaters.put("username", value -> existingUser.setUsername((String) value));
        fieldUpdaters.put("password", value -> existingUser.setPassword(passwordEncoder.encode((String) value)));
        fieldUpdaters.put("email", value -> existingUser.setEmail((String) value));
        fieldUpdaters.put("phone", value -> existingUser.setPhone((String) value));
        updates.forEach((field, value) -> {
            Consumer<Object> updater = fieldUpdaters.get(field);
            if (updater != null) {
                updater.accept(value);  
            }
        });
        return userRepository.save(existingUser);
    }


    /**
     * Deletes a user by their username.
     *
     * @param username The username of the user to be deleted.
     * @throws ResourceNotFoundException if no user is found with the given username.
     */
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
    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks if a user exists by their email.
     *
     * @param email The email to check.
     * @return true if the user exists, false otherwise.
     */
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Checks if a user exists by their phone number.
     *
     * @param phone The phone number to check.
     * @return true if the user exists, false otherwise.
     */
    public boolean userExistsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    /**
     * Checks if a user has admin role.
     *
     * @param username The username of the user to check.
     * @return true if the user is an admin, false otherwise.
     */
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
    @Transactional
    public void manageAdmin(String username, boolean isAdmin) {
        User user = getUserByUsername(username);
        if (user.getRole().isAdmin() == isAdmin) {
            throw new UnnecessaryUpdateException("User already has the desired role: " + (isAdmin ? "ADMIN" : "USER"));
        }
        user.setRole(isAdmin ? Role.ADMIN : Role.USER);
        userRepository.save(user); 
    }

    public boolean validatePassword(String username, String password) {
        User user = getUserByUsername(username);
        return passwordEncoder.matches(password, user.getPassword());
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return A list of all users.
     */
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

    private void validateUserForDuplicates(Map<String, Object> updates) {
        if (updates.containsKey("username")) {
            String username = (String) updates.get("username");
            if (userRepository.existsByUsername(username)) {
                throw new DuplicateResourceException("Username already exists:" + " \"" + username + "\"", "username");
            }
        }
        if (updates.containsKey("email")) {
            String email = (String) updates.get("email");
            if (userRepository.existsByEmail(email)) {
                throw new DuplicateResourceException("Email already exists:" + " \"" + email + "\"", "email");
            }
        }
        if (updates.containsKey("phone")) {
            String phone = (String) updates.get("phone");
            if (userRepository.existsByPhone(phone)) {
                throw new DuplicateResourceException("Phone number already exists:" + " \"" + phone + "\"", "phone");
            }
        }
    }

    private User encryptUser(User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        return user;
    }
}
