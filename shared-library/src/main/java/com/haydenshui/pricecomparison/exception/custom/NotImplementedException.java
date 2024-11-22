package com.haydenshui.pricecomparison.shared.exception.custom;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
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
