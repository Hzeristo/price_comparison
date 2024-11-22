package com.haydenshui.pricecomparison.shared.exception.custom;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
@Setter
@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    private String message;
    private String type;

    public ResourceNotFoundException(String message, String type) {
        super(message);
        this.message = message;
        this.type = type;
    }
}
