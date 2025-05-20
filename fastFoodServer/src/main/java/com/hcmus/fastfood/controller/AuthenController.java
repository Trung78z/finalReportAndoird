package com.hcmus.fastfood.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmus.fastfood.dto.UserLoginDTO;
import com.hcmus.fastfood.dto.UserRegisterDTO;
import com.hcmus.fastfood.mapper.UserMapper;
import com.hcmus.fastfood.model.User;
import com.hcmus.fastfood.service.AuthenService;
import com.hcmus.fastfood.utils.ResponseEntityUtils;

@RestController
@RequestMapping("/api/auth")
public class AuthenController {

    @Autowired
    private AuthenService authenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            String token = authenService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
            return ResponseEntityUtils.success(token);
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
                userRegisterDTO.getEmail()
            );
            return ResponseEntityUtils.created(UserMapper.toDTO(createdUser));
        } catch (RuntimeException ex) {
            return ResponseEntityUtils.error(ex.getMessage(), null);
        } catch (Exception ex) {
            return ResponseEntityUtils.serverError("Server error", null);
        }
    }
}