package com.haydenshui.pricecomparison.shared.exception;

import com.haydenshui.pricecomparison.shared.util.ApiResponse;
import com.haydenshui.pricecomparison.shared.exception.custom.*;
import com.haydenshui.pricecomparison.shared.model.Platform;
import jakarta.validation.ConstraintViolationException;
import java.net.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import io.jsonwebtoken.ExpiredJwtException;

/**
 * Global exception handler for the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse<String>> handleExpiredJwtException(ExpiredJwtException ex) {
        System.out.println("Handling ExpiredJwtException: " + ex.getMessage());
        ApiResponse<String> response = ApiResponse.failure("Token expired: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
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
