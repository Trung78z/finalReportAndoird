package com.hcmus.fastfood.controller;

import com.hcmus.fastfood.dto.UserDTO;
import com.hcmus.fastfood.mapper.UserMapper;
import com.hcmus.fastfood.model.User;
import com.hcmus.fastfood.repositories.UserRepo;
import com.hcmus.fastfood.utils.JwtUtil;
import com.hcmus.fastfood.utils.ResponseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserInfoController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntityUtils.error("Missing or invalid Authorization header", null);
            }
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            User user = userRepo.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            UserDTO userDTO = UserMapper.toDTO(user);
            return ResponseEntityUtils.success(userDTO);
        } catch (Exception ex) {
            return ResponseEntityUtils.error("Invalid token or user not found", null);
        }
    }
}