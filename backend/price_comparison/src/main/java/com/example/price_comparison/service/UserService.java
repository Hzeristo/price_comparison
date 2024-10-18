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

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Transactional
    public User createUser(User user) {
        validateUserForDuplicates(user);
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public User getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new UserNotFoundException("Can't find user named \"" + username + "\""));
    }

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

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteUser(String username) {
        User existingUser = getUserByUsername(username);
        userRepository.delete(existingUser);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public boolean userExistsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    private boolean isUserAdmin(String username) {
        User existingUser = getUserByUsername(username);
        return existingUser.getRole().isAdmin();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void manageAdmin(String username, boolean isAdmin) {
        User user = getUserByUsername(username);
        if (user.getRole().isAdmin() == isAdmin) {
            throw new UnnecessaryUpdateException("User already has the desired role");
        }
        user.setRole(isAdmin ? Role.ADMIN : Role.USER);
        userRepository.save(user); 
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    private void validateUserForDuplicates(User user) {
        String username = user.getUsername();
        String email = user.getEmail();
        String phone = user.getPhone();
        if (userExistsByUsername(username)) {
            throw new DuplicateResourceException("Username already exists.", "username");
        }
        if (userExistsByEmail(email)) {
            throw new DuplicateResourceException("Email already exists.", "email");
        }
        if (userExistsByPhone(phone)) {
            throw new DuplicateResourceException("Phone number already exists.", "phone");
        }
    }
}
