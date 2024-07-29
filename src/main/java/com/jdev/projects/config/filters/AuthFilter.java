package com.jdev.projects.config.filters;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdev.projects.config.managers.AuthManager;
import com.jdev.projects.model.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthFilter extends UsernamePasswordAuthenticationFilter {
    
    private AuthManager authManager;

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request, 
        HttpServletResponse response
    ) throws AuthenticationException {

        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            return authManager.authenticate(authentication);

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(failed.getMessage());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        String secretKey = "d6ee92b2bd4a7d7c51bd248b209506b0b3fcacff9307509153639f2dacdc2b09585d84024f6532e4b4b58c887b9adbb04ef6cc574de4e36b646f2ddfe40bb86e";
        Long tokenExpiry = System.currentTimeMillis() + 1000;
        String token = JWT.create()
                .withSubject(authResult.getPrincipal().toString())
                .withExpiresAt(new Date(tokenExpiry))
                .sign(Algorithm.HMAC512(secretKey));

        Map<String, Object> responseBody = Map.of(
            "token", token,
            "username", authResult.getPrincipal().toString(),
            "name", authResult.getName().toString(),
            "expiresAt", tokenExpiry
        );

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(responseBody));
        response.getWriter().flush();
    }
}