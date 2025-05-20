package com.hcmus.fastfood.service;

import com.hcmus.fastfood.model.User;
import com.hcmus.fastfood.repositories.UserRepo;
import com.hcmus.fastfood.repositories.UserRoleRepo;
import com.hcmus.fastfood.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserRoleRepo userRoleRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(String username, String password) {
        User user = userRepo.findByUsername(username).orElse(null);
        if (user == null || !user.isActive() || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
        return jwtUtil.generateToken(userDetails);
    }

    public User register(String username, String password, String email) {
        if (userRepo.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepo.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .isActive(true)
                .build();
        user.setRole(
                userRoleRepo.findByName("CUSTOMER")
                        .orElseThrow(() -> new RuntimeException("Role not found")));

        return userRepo.save(user);
    }
}