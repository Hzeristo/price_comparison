package com.example.price_comparison.service;

import com.example.price_comparison.model.*;
import com.example.price_comparison.repository.UserRepository;
import com.example.price_comparison.exception.custom.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User updateUser(String username, User user) {
        User existingUser = getUserByUsername(username);
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setRole(user.getRole());
        userRepository.save(existingUser);
        return existingUser;
    }

    public void deleteUser(String username) {
        User existingUser = getUserByUsername(username);
        userRepository.delete(existingUser);
    }

    public boolean userExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean userExistsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    private boolean isUserAdmin(String username) {
        User existingUser = getUserByUsername(username);
        return existingUser.getRole().isAdmin();
    }

    public void manageAdmin(String username, boolean isAdmin) {
        User user = getUserByUsername(username);
        if(user.getRole().isAdmin() == isAdmin) {
            throw new UnnecessaryUpdateException("User already has the desired role");
        }
        user.setRole(isAdmin ? Role.ADMIN : Role.USER);
        userRepository.save(user); 
    }
}
