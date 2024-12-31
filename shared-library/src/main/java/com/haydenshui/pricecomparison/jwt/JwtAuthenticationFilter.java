package com.haydenshui.pricecomparison.shared.jwt;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.haydenshui.pricecomparison.shared.exception.custom.JwtExpiredException;
import com.haydenshui.pricecomparison.shared.jwt.JwtTokenProvider;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                try {
                    if ("/user/validate".equals(request.getRequestURI())) {
                        System.out.println("Skipping JWT filter for /validate");
                        filterChain.doFilter(request, response);
                        return;
                    }
            
                    if ("/user/new".equals(request.getRequestURI())) {
                        System.out.println("Skipping JWT filter for /new");
                        filterChain.doFilter(request, response);
                        return;
                    }

                    if ("/items/spider".equals(request.getRequestURI())) {
                        System.out.println("Skipping JWT filter for /item/spider");
                        filterChain.doFilter(request, response);
                        return;
                    }

                    if ("/favorite/test".equals(request.getRequestURI())) {
                        System.out.println("Skipping JWT filter for /favorite/test");
                        filterChain.doFilter(request, response);
                        return;
                    }

                    if ("/spider/test".equals(request.getRequestURI())) {
                        System.out.println("Skipping JWT filter for /spider/test");
                        filterChain.doFilter(request, response);
                        return;
                    }

                    if ("/items/test".equals(request.getRequestURI())) {
                        System.out.println("Skipping JWT filter for /items/test");
                        filterChain.doFilter(request, response);
                        return;
                    }
                    
                    if (request.getMethod().equals("OPTIONS")) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        System.out.println("options");
                        filterChain.doFilter(request, response);
                        return;
                    }
            
                    String token = request.getHeader("Authorization");
            
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);
                        System.out.println("Processing token: " + token);
                    
                        if (jwtTokenProvider.validateToken(token)) {
                            System.out.println("Token is valid");
                    
                            String username = jwtTokenProvider.getUsernameFromToken(token);
                            List<String> roles = jwtTokenProvider.getRolesFromToken(token);
                    
                            System.out.println("Username: " + username);
                            System.out.println("Roles: " + roles);
                    
                            if (username != null && roles != null) {
                                List<SimpleGrantedAuthority> authorities = roles.stream()
                                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Ensure proper role prefix
                                        .collect(Collectors.toList());
                    
                                UsernamePasswordAuthenticationToken authentication =
                                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                    
                                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                System.out.println("Authentication in SecurityContext: " + authentication);
                            } else {
                                System.out.println("Username or roles are null");
                            }
                        } else {
                            System.out.println("Invalid token");
                        }
                    }
            
                    // Continue with the filter chain
                    filterChain.doFilter(request, response);
            
                } catch (JwtExpiredException ex) {
                    System.out.println("JWT token has expired: " + ex.getMessage());
                    handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired");
                }
    }

    private void handleException(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"status\":" + status + ",\"message\":\"" + message + "\"}");
    }
}
