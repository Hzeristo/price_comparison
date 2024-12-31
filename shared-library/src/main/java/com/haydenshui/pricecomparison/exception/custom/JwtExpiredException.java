package com.haydenshui.pricecomparison.shared.exception.custom;

import com.haydenshui.pricecomparison.shared.model.Platform;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
@Setter
@NoArgsConstructor
public class JwtExpiredException extends RuntimeException {
    private String message;

    public JwtExpiredException(String message) {
        super(message);
    }
}
