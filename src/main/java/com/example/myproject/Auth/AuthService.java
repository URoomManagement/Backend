package com.example.myproject.Auth;

import com.example.myproject.Helper.JwtUtil;
import com.example.myproject.Auth.dto.LoginRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    public ResponseEntity<?> login(LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

            // HTTP-only cookies
            addTokenCookie(response, "access_token", accessToken, (int) (1 * 60 * 60)); // 1 hour
            addTokenCookie(response, "refresh_token", refreshToken, (int) (1 * 24 * 60 * 60)); // 1 day

            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractTokenFromCookie(request, "refresh_token");
        
        if (refreshToken != null && jwtUtil.validateRefreshToken(refreshToken)) {
            String username = jwtUtil.extractUsernameFromRefreshToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            String newAccessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
            addTokenCookie(response, "access_token", newAccessToken, (int) (1 * 60 * 60)); // 1 hour
            
            return ResponseEntity.ok().build();
        }
        
        return ResponseEntity.status(401).body("Invalid refresh token");
    }

    public ResponseEntity<?> logout(HttpServletResponse response) {
        deleteCookie(response, "access_token");
        deleteCookie(response, "refresh_token");
        return ResponseEntity.ok().build();
    }

    private void addTokenCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        // cookie.setSecure(true);  // HTTPS
        
        if (name.equals("refresh_token")) {
            cookie.setPath("/api/auth/refresh");
        } else {
            cookie.setPath("/");
        }
        
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    private void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        // cookie.setSecure(true);  // HTTPS
        
        if (name.equals("refresh_token")) {
            cookie.setPath("/api/auth/refresh");
        } else {
            cookie.setPath("/");
        }
        
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
} 