package com.haydenshui.pricecomparison.favorite.config;

import com.haydenshui.pricecomparison.shared.jwt.JwtAuthenticationFilter;
import com.haydenshui.pricecomparison.shared.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secretKey, expiration); // 通过构造方法传递参数
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/favorite/**").hasAnyRole("USER", "ADMIN")
                    .anyRequest().authenticated() // 其他请求需要认证
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider()), UsernamePasswordAuthenticationFilter.class) // 添加JWT认证过滤器
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 禁用session，使用无状态身份验证

        return http.build();
    }
}

