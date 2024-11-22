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
        ApiResponse<String> response = ApiResponse.failure(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handles IllegalArgumentException.
     *
     * @param ex the exception thrown
     * @return a standardized API response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
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
        ApiResponse<String> response = ApiResponse.failure("An error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
