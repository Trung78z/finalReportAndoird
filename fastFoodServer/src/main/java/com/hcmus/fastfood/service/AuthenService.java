package com.hcmus.fastfood.service;

import com.hcmus.fastfood.dto.ChangePasswordDTO;
import com.hcmus.fastfood.model.User;
import com.hcmus.fastfood.repositories.UserRepository;
import com.hcmus.fastfood.repositories.UserRoleRepo;
import com.hcmus.fastfood.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserRoleRepo userRoleRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String loginByEmail(String email, String password) {
        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null || !user.isActive() || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        String roleName = user.getRole() != null ? user.getRole().getName() : "USER";
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(roleName)
                .build();
        return jwtUtil.generateToken(userDetails);
    }

    public User getUserNameByEmail(String email) {
        User user = userRepo.findByEmail(email).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
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

    public void changePassword(String username, ChangePasswordDTO changePasswordDTO) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepo.save(user);
    }

    public String refreshAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("Missing refresh token");
        }
        String username = jwtUtil.extractUsername(refreshToken);
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
        return jwtUtil.generateToken(userDetails);
    }
}