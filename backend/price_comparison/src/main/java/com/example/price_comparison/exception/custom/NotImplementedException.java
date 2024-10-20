package com.example.price_comparison.exception.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
@Getter
@Setter
@NoArgsConstructor
public class NotImplementedException extends RuntimeException {
    private String message;

    public NotImplementedException(String message) {
        super(message);
        this.message = message;
    }
}
