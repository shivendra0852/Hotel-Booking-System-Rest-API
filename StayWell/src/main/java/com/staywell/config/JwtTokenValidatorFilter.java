package com.staywell.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;

@Component
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private final Key secretKey;

    public JwtTokenValidatorFilter(Key secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader(SecurityConstants.JWT_HEADER);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring(7);

                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token);

                Claims body = claimsJws.getBody();
                String username = body.getSubject();

                // You can perform further validation or processing here
                // and set additional details in the request if needed.

                request.setAttribute("username", username);
            } catch (Exception e) {
                // Token validation failed
                // You can handle the exception according to your application's requirements.
                // For example, you can send a custom error response or redirect the user to a login page.
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
