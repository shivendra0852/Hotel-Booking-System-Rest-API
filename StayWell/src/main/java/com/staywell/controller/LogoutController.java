package com.staywell.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/staywell")
public class LogoutController {

    @PostMapping("/admins/logout")
    public ResponseEntity<String> adminLogoutHandler(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        return performLogout(request, response, auth);
    }

    @PostMapping("/customers/logout")
    public ResponseEntity<String> customerLogoutHandler(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        return performLogout(request, response, auth);
    }

    @PostMapping("/hotels/logout")
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
