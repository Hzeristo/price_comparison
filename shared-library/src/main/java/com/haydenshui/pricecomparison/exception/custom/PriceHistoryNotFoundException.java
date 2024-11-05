package com.haydenshui.pricecomparison.shared.exception.custom;



import com.haydenshui.pricecomparison.shared.model.Platform;
import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Getter
@Setter
@NoArgsConstructor
public class PriceHistoryNotFoundException  extends RuntimeException {
    private String message;

    public PriceHistoryNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
