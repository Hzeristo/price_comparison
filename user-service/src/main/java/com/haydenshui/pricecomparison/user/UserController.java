package com.haydenshui.pricecomparison.user;

import com.haydenshui.pricecomparison.shared.model.Platform;
import com.haydenshui.pricecomparison.shared.model.User;
import com.haydenshui.pricecomparison.shared.util.ApiResponse;
import com.haydenshui.pricecomparison.shared.exception.custom.*;
import com.haydenshui.pricecomparison.shared.jwt.*;
import jakarta.validation.Valid;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Controller class for managing user-related API endpoints.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

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
            @RequestParam @NotNull String username) {
        User user = userService.getUserByUsername(username);
        user.setPassword(user.getPassword());
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
            @RequestParam @NotNull String email) {
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
            @RequestParam @NotNull String phone) {
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
            @RequestParam String username,
            @RequestBody Map<String, Object> updates) {
        User updatedUser = userService.updateUser(username, updates);
        return ResponseEntity.ok(ApiResponse.success(updatedUser));
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<String>> validateUser(
            @RequestParam String username,
            @RequestParam String password) {
        if (userService.validatePassword(username, password)) {
            ArrayList<String> roles = new ArrayList<>();
            roles.add("USER");
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey, expiration);
            String token = jwtTokenProvider.generateToken(username, roles);
            return ResponseEntity.ok(ApiResponse.success(token));
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
            @RequestParam @NotNull String username,
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
            @RequestParam @NotNull String username) {
        userService.deleteUser(username);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    
    /**
     * Handles ConstraintViolationException.
     *
     * @param ex the exception thrown
     * @return a standardized API response
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        System.out.println("Handling ConstraintViolationException: " + ex.getMessage());
        ApiResponse<String> response = ApiResponse.failure("Invalid input for database: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles custom DictionaryNotInitializedException.
     *
     * @param ex the exception thrown
     * @return a standardized API response
     */
    @ExceptionHandler(DictionaryNotInitializedException.class)
    public ResponseEntity<ApiResponse<Platform>> handleDictionaryNotInitializedException(DictionaryNotInitializedException ex) {
        System.out.println("Handling DictionaryNotInitializedException: " + ex.getMessage());
        ApiResponse<Platform> response = ApiResponse.failure(ex.getMessage(), ex.getPlatform());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * Handles custom DuplicateResourceException.
     *
     * @param ex the exception thrown
     * @return a standardized API response
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<String>> handleDuplicateResourceException(DuplicateResourceException ex) {
        System.out.println("Handling DuplicateResourceException: " + ex.getMessage());
        ApiResponse<String> response = ApiResponse.failure(ex.getMessage(), ex.getType());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handles custom NotImplementedException.
     *
     * @param ex the exception thrown
     * @return a standardized API response
     */
    @ExceptionHandler(NotImplementedException.class)
    public ResponseEntity<ApiResponse<String>> handleNotImplementedException(NotImplementedException ex) {
        System.out.println("Handling NotImplementedException: " + ex.getMessage());
        ApiResponse<String> response = ApiResponse.failure(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(response);
    }

    /**
     * Handles custom UnauthorizedException.
     *
     * @param ex the exception thrown
     * @return a standardized API response
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<String>> handleUnauthorizedException(UnauthorizedException ex) {
        System.out.println("Handling UnauthorizedException: " + ex.getMessage());
        ApiResponse<String> response = ApiResponse.failure(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * Handles custom UnnecessaryUpdateException.
     *
     * @param ex the exception thrown
     * @return a standardized API response
     */
    @ExceptionHandler(UnnecessaryUpdateException.class)
    public ResponseEntity<ApiResponse<String>> handleUnnecessaryUpdateException(UnnecessaryUpdateException ex) {
        System.out.println("Handling UnnecessaryUpdateException: " + ex.getMessage());
        ApiResponse<String> response = ApiResponse.failure(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(JwtExpiredException.class)
    public ResponseEntity<ApiResponse<String>> handleExpiredJwtException(JwtExpiredException ex) {
        System.out.println("Handling JwtExpired: " + ex.getMessage());
        ApiResponse<String> response = ApiResponse.failure(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles IllegalArgumentException.
     *
     * @param ex the exception thrown
     * @return a standardized API response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        System.out.println("Handling IllegalArgumentException: " + ex.getMessage());
        ApiResponse<String> response = ApiResponse.failure("Invalid input for matching: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles generic exceptions.
     *
     * @param ex the exception thrown
     * @return a standardized API response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        System.out.println("Handling generic exception: " + ex.getMessage());
        ApiResponse<String> response = ApiResponse.failure("An error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
