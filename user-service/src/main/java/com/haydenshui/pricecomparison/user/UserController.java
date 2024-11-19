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
import java.util.Map;
import java.util.HashMap;

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
     * @url GET /user/username
     */
    @GetMapping("/username")
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
    @GetMapping("/email")
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
    @GetMapping("/phone")
    public ResponseEntity<ApiResponse<Boolean>> userExistsByPhone(
            @RequestParam @NotNull @ValidPhone String phone) {
        boolean exists = userService.userExistsByPhone(phone);
        return ResponseEntity.ok(ApiResponse.success(exists));
    }

    /**
     * Updates user fields.
     * @param username The username of the user to be updated.
     * @param newUsername The new username.
     * @param newPassword The new password.
     * @param newEmail The new email.
     * @param newPhone The new phone number.
     * @return The updated user.
     * @url PUT /user/update
     */
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<User>> updateUserFields(
            @RequestParam @ValidUsername String username,
            @RequestBody Map<String, Object> updates) {
        User updatedUser = userService.updateUser(username, updates);
        return ResponseEntity.ok(ApiResponse.success(updatedUser));
    }

    @GetMapping("validate")
    public ResponseEntity<ApiResponse<User>> validateUser(
            @RequestParam @NotNull @ValidUsername String username,
            @RequestParam @NotNull @ValidPassword String password) {
        if (userService.validatePassword(username, password)) {
            return ResponseEntity.ok(ApiResponse.success(userService.getUserByUsername(username)));
        } else {
            return ResponseEntity.ok(ApiResponse.failure("Invalid password"));
        }
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
