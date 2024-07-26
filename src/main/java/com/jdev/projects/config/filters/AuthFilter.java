package com.jdev.projects.config.filters;

import java.io.IOException;
import java.util.Date;

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
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(),
                    user.getPassword());
            return authManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    //when a user can not be authenticated by AuthManager then unsuccessfulAuthentication method will run and return 
    //a response with appropriate error code
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(failed.getMessage());
        response.getWriter().flush();
    }

    //When user is authenticated by AuthManager then successfulAuthentication method will run 
    //We first create a JWT token using the secreteKey(256Bit)
    //We set the response header "Authorization", "Bearer " + token 
    //And finally we return this response to the user.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        String secretKey = "d6ee92b2bd4a7d7c51bd248b209506b0b3fcacff9307509153639f2dacdc2b09585d84024f6532e4b4b58c887b9adbb04ef6cc574de4e36b646f2ddfe40bb86e";
        String token = JWT.create()
                //Here again getPrinciple returns an Object and we need it to be a string so calling toString on it.
                .withSubject(authResult.getPrincipal().toString())
                //Here we pass a new Date object which created by using current system time in millies
                //We add the time in millies at which we want JWT token to expire. In this case 2 hours in milies is 7200000
                .withExpiresAt(new Date(System.currentTimeMillis() + 7200000))
                .sign(Algorithm.HMAC512(secretKey));
        response.setHeader("Authorization", "Bearer " + token);

    }

}
