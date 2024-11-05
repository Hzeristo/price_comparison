package com.haydenshui.pricecomparison.shared.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A standard response object for API responses.
 *
 * @param <T> the type of data contained in the response
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;      // Indicates if the request was successful
    private String message;       // A message describing the outcome
    private T data;               // The data returned in the response

    // Static factory methods for convenience
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Request was successful", data);
    }

    public static <T> ApiResponse<T> failure(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public static <T> ApiResponse<T> failure(String message, T data) {
        return new ApiResponse<>(false, message, data);
    }
}
