package com.hcmus.fastfood.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmus.fastfood.dto.ChangePasswordDTO;
import com.hcmus.fastfood.dto.UserLoginDTO;
import com.hcmus.fastfood.dto.UserRegisterDTO;
import com.hcmus.fastfood.model.User;
import com.hcmus.fastfood.service.AuthenService;
import com.hcmus.fastfood.utils.JwtUtil;
import com.hcmus.fastfood.utils.ResponseEntityUtils;

@RestController
@RequestMapping("/api/auth")
public class AuthenController {

    @Autowired
    private AuthenService authenService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO,
            jakarta.servlet.http.HttpServletResponse response) {
        try {
            // Change login to use email and password
            String accessToken = authenService.loginByEmail(userLoginDTO.getEmail(), userLoginDTO.getPassword());
            String refreshToken = jwtUtil.generateRefreshToken(userLoginDTO.getEmail());

            // Set refresh token as HttpOnly cookie
            jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("refreshToken", refreshToken);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
            response.addCookie(cookie);

            Map<String, Object> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);

            return ResponseEntityUtils.success(tokens);
        } catch (RuntimeException ex) {
            return ResponseEntityUtils.error(ex.getMessage(), null);
        } catch (Exception ex) {
            return ResponseEntityUtils.serverError("Server error", null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            User createdUser = authenService.register(
                    userRegisterDTO.getUsername(),
                    userRegisterDTO.getPassword(),
                    userRegisterDTO.getEmail());
            return ResponseEntityUtils.created(createdUser);
        } catch (RuntimeException ex) {
            return ResponseEntityUtils.error(ex.getMessage(), null);
        } catch (Exception ex) {
            return ResponseEntityUtils.serverError("Server error", null);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        try {
            String newAccessToken = authenService.refreshAccessToken(refreshToken);

            Map<String, Object> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);

            return ResponseEntityUtils.success(tokens);
        } catch (RuntimeException ex) {
            return ResponseEntityUtils.error(ex.getMessage(), null);
        } catch (Exception ex) {
            return ResponseEntityUtils.error("Invalid refresh token or user not found", null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(jakarta.servlet.http.HttpServletResponse response) {
        // Remove the refresh token cookie
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("refreshToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Expire immediately
        response.addCookie(cookie);

        return ResponseEntityUtils.success("Logged out successfully");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            authenService.changePassword(username, changePasswordDTO);

            return ResponseEntityUtils.success("Password changed successfully");
        } catch (RuntimeException ex) {
            return ResponseEntityUtils.error(ex.getMessage(), null);
        } catch (Exception ex) {
            return ResponseEntityUtils.error("Failed to change password", null);
        }
    }
}