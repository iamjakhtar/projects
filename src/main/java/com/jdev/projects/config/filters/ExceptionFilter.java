package com.jdev.projects.config.filters;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.lang.NonNull;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
                try {
                    filterChain.doFilter(request, response);
                } catch (EntityNotFoundException e) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("User not found");
                    response.getWriter().flush();
                } 
                catch (RuntimeException e) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Bad request");
                    response.getWriter().flush();
                }
    }
    
}
