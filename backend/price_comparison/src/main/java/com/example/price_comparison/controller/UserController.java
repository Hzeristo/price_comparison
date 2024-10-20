package com.example.price_comparison.controller;

import com.example.price_comparison.model.User;
import com.example.price_comparison.service.UserService;
import com.example.price_comparison.util.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Controller class for managing user-related API endpoints.
 */
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users.
     *
     * @return A list of all users.
     * @url GET /user/all
     */
    @GetMapping("/user/all")
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    /**
     * Creates a new user.
     *
     * @param user The user to be created.
     * @return The created user.
     * @url POST /user/new
     */
    @PostMapping("/user/new")
    public ApiResponse<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ApiResponse.success(createdUser);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user.
     * @return The user associated with the given username.
     * @url GET /user/username?username={username}
     */
    @GetMapping("/user/username")
    public ApiResponse<User> getUserByUsername(
        @RequestParam
        @NotNull(message = "Username cannot be null")
        @NotBlank(message = "Username cannot be empty")
        @Pattern(regexp = "^[a-zA-Z0-9_]{5,255}$", message = "Username should be between 5 and 255 characters and can only contain letters, numbers, and underscores")
        String username) {
        User user = userService.getUserByUsername(username);
        return ApiResponse.success(user);
    }

    /**
     * Checks if a user exists by their email.
     *
     * @param email The email to check.
     * @return true if the user exists, false otherwise.
     * @url GET /user/email?email={email}
     */
    @GetMapping("/user/email")
    public ApiResponse<Boolean> userExistsByEmail(
        @RequestParam
        @NotNull(message = "Email cannot be null")
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email should be valid")
        String email) {
        boolean exists = userService.userExistsByEmail(email);
        return ApiResponse.success(exists);
    }

    /**
     * Checks if a user exists by their phone number.
     *
     * @param phone The phone number to check.
     * @return true if the user exists, false otherwise.
     * @url GET /user/phone?phone={phone}
     */
    @GetMapping("/user/phone")
    public ApiResponse<Boolean> userExistsByPhone(
        @RequestParam 
        @NotNull(message = "Phone cannot be null")
        @NotBlank(message = "Phone cannot be empty")
        @Pattern(regexp = "^\\d{13}$", message = "Phone number should be 13 digits")
        String phone) {
        boolean exists = userService.userExistsByPhone(phone);
        return ApiResponse.success(exists);
    }

    /**
     * Updates an existing user's information.
     *
     * @param username The username of the user to be updated.
     * @param user The user object containing updated information.
     * @return The updated user.
     * @url PUT /user/all?username={username}
     */
    @PutMapping("/user/all")
    public ApiResponse<User> updateUser(@PathVariable String username, @Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(username, user);
        return ApiResponse.success(updatedUser);
    }

    /**
     * Updates a user's username.
     *
     * @param username The username of the user.
     * @param newUsername The new username.
     * @return The updated user.
     * @url PUT /user/username?username={username}&newUsername={newUsername}
     */
    @PutMapping("/user/username")
    public ApiResponse<User> updateUsername(@PathVariable String username, 
        @RequestParam 
        @NotNull(message = "Username cannot be null")
        @NotBlank(message = "Username cannot be empty")
        @Pattern(regexp = "^[a-zA-Z0-9_]{5,255}$", message = "Username should be between 5 and 255 characters and can only contain letters, numbers, and underscores")
        String newUsername) {
            User user = userService.getUserByUsername(username);
            user.setUsername(newUsername);
            User updatedUser = userService.updateUser(username, user);
            return ApiResponse.success(updatedUser);
    }

    /**
     * Updates a user's password.
     *
     * @param username The username of the user.
     * @param password The new password.
     * @return The updated user.
     * @url PUT /user/pwd?username={username}&password={password}
     */
    @PutMapping("/user/pwd")
    public ApiResponse<User> updatePassword(@PathVariable String username, 
        @RequestParam 
        @NotNull(message = "Password cannot be null")
        @NotBlank(message = "Password cannot be empty")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,255}$", message = "Password should be between 8 and 255 characters and contain at least one uppercase letter, one lowercase letter, and one digit")
        String password) {
        User user = userService.getUserByUsername(username);
        user.setPassword(password);
        User updatedUser = userService.updateUser(username, user);
        return ApiResponse.success(updatedUser);
    }

    /**
     * Updates a user's email.
     *
     * @param username The username of the user.
     * @param email The new email.
     * @return The updated user.
     * @url PUT /user/email?username={username}&email={email}
     */
    @PutMapping("/user/email")
    public ApiResponse<User> updateEmail(@PathVariable String username, 
        @RequestParam 
        @NotNull(message = "Email cannot be null")
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Email should be valid")
        String email) {
        User user = userService.getUserByUsername(username);
        user.setEmail(email);
        User updatedUser = userService.updateUser(username, user);
        return ApiResponse.success(updatedUser);
    }

    /**
     * Updates a user's phone number.
     *
     * @param username The username of the user.
     * @param phone The new phone number.
     * @return The updated user.
     * @url PUT /user/phone?username={username}&phone={phone}
     */
    @PutMapping("/user/phone")
    public ApiResponse<User> updatePhone(@PathVariable String username, 
        @RequestParam 
        @NotNull(message = "Phone cannot be null")
        @NotBlank(message = "Phone cannot be empty")
        @Pattern(regexp = "^\\d{13}$", message = "Phone number should be 13 digits")
        String phone) {
        User user = userService.getUserByUsername(username);
        user.setPhone(phone);
        User updatedUser = userService.updateUser(username, user);
        return ApiResponse.success(updatedUser);
    }

    /**
     * Manages a user's admin role.
     *
     * @param username The username of the user.
     * @param isAdmin Whether the user should be an admin or not.
     * @return A success response.
     * @url PUT /user/admin?username={username}&isAdmin={isAdmin}
     */
    @PutMapping("/user/admin")
    public ApiResponse<User> manageAdmin(@PathVariable String username, @RequestParam boolean isAdmin) {
        userService.manageAdmin(username, isAdmin);
        return ApiResponse.success(null);
    }

    /**
     * Deletes a user by their username.
     *
     * @param username The username of the user to be deleted.
     * @return A success response.
     * @url DELETE /user?username={username}
     */
    @DeleteMapping("/user")
    public ApiResponse<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ApiResponse.success(null);
    }
}
