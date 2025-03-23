package com.example.suhbatchi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    public JwtAuthFilter(UserDetailsService userDetailsService, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String requestURI = request.getRequestURI();
        final String authHeader = request.getHeader("Authorization");
        final String token;
        final String userId;


        log.info("uri : {}", requestURI);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // Agar token bo‘lmasa, tekshirmasdan requestni davom ettiramiz
        }
        log.info("authHeader : {}", authHeader);
        System.out.println("Token: [" + authHeader + "]");
        // Tokenni ajratib olish
        token = authHeader.substring(7);
        userId = jwtUtils.extractUsername(token);
        log.info("userId : {}", userId);
        if (jwtUtils.isTemporaryToken(token)) {
            if (request.getRequestURI().startsWith("/auth/") || request.getRequestURI().startsWith("/login/")) {
                log.info("userId : {}", "diyor");
                filterChain.doFilter(request, response);
                return;
            }
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Temporary token not allowed here");
            return;
        }


        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

            if (jwtUtils.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

}
