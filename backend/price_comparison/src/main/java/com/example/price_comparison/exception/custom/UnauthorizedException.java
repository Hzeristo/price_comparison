package com.example.price_comparison.exception.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@Getter
@Setter
@NoArgsConstructor
public class UnauthorizedException extends RuntimeException {
    private String message;
    
    public UnauthorizedException(String message) {
        super(message);
        this.message = message;
    }
}
