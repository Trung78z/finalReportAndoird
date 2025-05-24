package com.hcmus.fastfood.controller;

import com.hcmus.fastfood.dto.UserDTO;
import com.hcmus.fastfood.service.UserService;
import com.hcmus.fastfood.utils.ResponseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserInfoController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // Extracted from JWT access token
            UserDTO userDTO = userService.getUserFromUserName(username);
            return ResponseEntityUtils.success(userDTO);
        } catch (Exception ex) {
            return ResponseEntityUtils.error("Invalid token or user not found", null);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
        try {
            return ResponseEntityUtils.success(userService.getAllUsers());
        } catch (Exception ex) {
            return ResponseEntityUtils.error("Failed to get users: " + ex.getMessage(), null);
        }
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentUser(
            @RequestBody UserDTO userDTO,
            HttpServletRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // Extracted from JWT access token
            UserDTO updatedUser = userService.updateCurrentUser(username, userDTO);
            return ResponseEntityUtils.success(updatedUser);
        } catch (Exception ex) {
            return ResponseEntityUtils.error("Update failed: " + ex.getMessage(), null);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO,
            HttpServletRequest request) {
        try {
            UserDTO updatedUser = userService.updateUser(id, userDTO);
            return ResponseEntityUtils.success(updatedUser);
        } catch (Exception ex) {
            return ResponseEntityUtils.error("Update failed: " + ex.getMessage(), null);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @PathVariable Long id,
            HttpServletRequest request) {
        try {
            userService.deleteUser(id);
            return ResponseEntityUtils.success("User deleted successfully");
        } catch (Exception ex) {
            return ResponseEntityUtils.error("Delete failed: " + ex.getMessage(), null);
        }
    }
}