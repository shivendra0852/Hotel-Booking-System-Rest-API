package com.staywell.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/staywell")
public class LogoutController {

    @GetMapping("/admin/logout")
    public ResponseEntity<String> adminLogoutHandler(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        return performLogout(request, response, auth);
    }

    @GetMapping("/customer/logout")
    public ResponseEntity<String> customerLogoutHandler(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        return performLogout(request, response, auth);
    }

    @GetMapping("/hotel/logout")
    public ResponseEntity<String> hotelLogoutHandler(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        return performLogout(request, response, auth);
    }

    private ResponseEntity<String> performLogout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            // If the user is not authenticated, return an error response
            return new ResponseEntity<>("You must be logged in to access this endpoint", HttpStatus.UNAUTHORIZED);
        }

        // Invalidate the session and clear the authentication
        new SecurityContextLogoutHandler().logout(request, response, auth);

        // Return a success response
        return new ResponseEntity<>("You have been logged out", HttpStatus.OK);
    }
}
