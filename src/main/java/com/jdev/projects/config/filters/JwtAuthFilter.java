package com.jdev.projects.config.filters;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain)
            throws ServletException, IOException {

                String header = request.getHeader("Authorization");

                if (header == null || !header.startsWith("Bearer")) {
                    doFilter(request, response, filterChain);
                    return;
                }

                String token = header.replace("Bearer ", "");
                String secretKey =
                "d6ee92b2bd4a7d7c51bd248b209506b0b3fcacff9307509153639f2dacdc2b09585d84024f6532e4b4b58c887b9adbb04ef6cc574de4e36b646f2ddfe40bb86e";

                String user = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token).getSubject();

                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, Arrays.asList());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(request, response);
    }
    
}