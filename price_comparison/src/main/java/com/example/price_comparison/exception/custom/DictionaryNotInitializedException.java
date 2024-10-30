package com.example.price_comparison.exception.custom;

import com.example.price_comparison.model.Platform;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
@Setter
@NoArgsConstructor
public class DictionaryNotInitializedException extends RuntimeException {
    private String message;
    private Platform platform;

    public DictionaryNotInitializedException(String message, Platform platform) {
        super(message);
        this.message = message;
        this.platform = platform;
    }
}
