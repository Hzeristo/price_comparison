package com.haydenshui.pricecomparison.user;

import com.haydenshui.pricecomparison.shared.model.User;
import com.haydenshui.pricecomparison.shared.util.ApiResponse;
import com.haydenshui.pricecomparison.shared.util.validation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Controller class for managing user-related API endpoints.
 */
@RestController
@RequestMapping("/user")
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
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<User>>> findAllUsers() {
        List<User> users = userService.findAllUsers(); // Fetch the list of users
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    /**
     * Creates a new user.
     *
     * @param user The user to be created.
     * @return The created user.
     * @url POST /user/new
     */
    @PostMapping("/new")
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(ApiResponse.success(createdUser));
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user.
     * @return The user associated with the given username.
     * @url GET /user/by-username
     */
    @GetMapping("/by-username")
    public ResponseEntity<ApiResponse<User>> getUserByUsername(
            @RequestParam @NotNull @ValidUsername String username) {
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    /**
     * Checks if a user exists by their email.
     *
     * @param email The email to check.
     * @return true if the user exists, false otherwise.
     * @url GET /user/check-email
     */
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> userExistsByEmail(
            @RequestParam @NotNull @ValidEmail String email) {
        boolean exists = userService.userExistsByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }

    /**
     * Checks if a user exists by their phone number.
     *
     * @param phone The phone number to check.
     * @return true if the user exists, false otherwise.
     * @url GET /user/check-phone
     */
    @GetMapping("/check-phone")
    public ResponseEntity<ApiResponse<Boolean>> userExistsByPhone(
            @RequestParam @NotNull @ValidPhone String phone) {
        boolean exists = userService.userExistsByPhone(phone);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }

    /**
     * Updates an existing user's information.
     *
     * @param username The username of the user to be updated.
     * @param user The user object containing updated information.
     * @return The updated user.
     * @url PUT /user/update
     */
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<User>> updateUser(
            @RequestParam @NotNull @ValidUsername String username,
            @Valid @RequestBody User user) {
        User updatedUser = userService.updateUser(username, user);
        return ResponseEntity.ok(ApiResponse.success(updatedUser));
    }

    /**
     * Updates a user's username.
     *
     * @param username The current username of the user.
     * @param newUsername The new username.
     * @return The updated user.
     * @url PUT /user/update-username
     */
    @PutMapping("/update-username")
    public ResponseEntity<ApiResponse<User>> updateUsername(
            @RequestParam @NotNull @ValidUsername String username,
            @RequestParam @NotNull @ValidUsername String newUsername) {
        User user = userService.getUserByUsername(username);
        user.setUsername(newUsername);
        User updatedUser = userService.updateUser(username, user);
        return ResponseEntity.ok(ApiResponse.success(updatedUser));
    }

    /**
     * Updates a user's password.
     *
     * @param username The username of the user.
     * @param password The new password.
     * @return The updated user.
     * @url PUT /user/update-password
     */
    @PutMapping("/update-password")
    public ResponseEntity<ApiResponse<User>> updatePassword(
            @RequestParam @NotNull @ValidUsername String username,
            @RequestParam @NotNull @ValidPassword String password) {
        User user = userService.getUserByUsername(username);
        user.setPassword(password);
        User updatedUser = userService.updateUser(username, user);
        return ResponseEntity.ok(ApiResponse.success(updatedUser));
    }

    /**
     * Updates a user's email.
     *
     * @param username The username of the user.
     * @param email The new email.
     * @return The updated user.
     * @url PUT /user/update-email
     */
    @PutMapping("/update-email")
    public ResponseEntity<ApiResponse<User>> updateEmail(
            @RequestParam @NotNull @ValidUsername String username,
            @RequestParam @NotNull @ValidEmail String email) {
        User user = userService.getUserByUsername(username);
        user.setEmail(email);
        User updatedUser = userService.updateUser(username, user);
        return ResponseEntity.ok(ApiResponse.success(updatedUser));
    }

    /**
     * Updates a user's phone number.
     *
     * @param username The username of the user.
     * @param phone The new phone number.
     * @return The updated user.
     * @url PUT /user/update-phone
     */
    @PutMapping("/update-phone")
    public ResponseEntity<ApiResponse<User>> updatePhone(
            @RequestParam @NotNull @ValidUsername String username,
            @RequestParam @NotNull @ValidPhone String phone) {
        User user = userService.getUserByUsername(username);
        user.setPhone(phone);
        User updatedUser = userService.updateUser(username, user);
        return ResponseEntity.ok(ApiResponse.success(updatedUser));
    }

    /**
     * Manages a user's admin role.
     *
     * @param username The username of the user.
     * @param isAdmin Whether the user should be an admin or not.
     * @return A success response.
     * @url PUT /user/manage-admin
     */
    @PutMapping("/manage-admin")
    public ResponseEntity<ApiResponse<User>> manageAdmin(
            @RequestParam @NotNull @ValidUsername String username,
            @RequestParam boolean isAdmin) {
        userService.manageAdmin(username, isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    /**
     * Deletes a user by their username.
     *
     * @param username The username of the user to be deleted.
     * @return A success response.
     * @url DELETE /user/delete
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @RequestParam @NotNull @ValidUsername String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
