package com.haydenshui.pricecomparison.auth;

import com.haydenshui.pricecomparison.shared.jwt.*;
import com.haydenshui.pricecomparison.shared.exception.custom.LoginFailureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;
    
    private final JwtTokenProvider jwtTokenProvider; // 用于生成 JWT

    public AuthController() {
        this.jwtTokenProvider = new JwtTokenProvider(secretKey, expiration);
    }

    @Autowired
    private RestTemplate restTemplate; // 用于调用 UserService 的登录接口

    @Value("${user.service.url}") // UserService 的 base URL
    private String userServiceUrl;

    @PostMapping("/login")
    public JwtAuthenticationResponse login(@RequestBody LoginRequest loginRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                userServiceUrl + "/user/validate",
                HttpMethod.POST,
                request,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            String token = jwtTokenProvider.generateToken(loginRequest.getUsername(), List.of("ROLE_USER"));
            return new JwtAuthenticationResponse(token);
        } else {
            throw new LoginFailureException("Invalid username or password");
        }
    }
}
