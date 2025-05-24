package com.hcmus.fastfood.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmus.fastfood.dto.UserDTO;
import com.hcmus.fastfood.mapper.UserMapper;
import com.hcmus.fastfood.model.User;
import com.hcmus.fastfood.repositories.UserRepository;
import com.hcmus.fastfood.utils.JwtUtil;

@Service
public class UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepo;

    public UserDTO getUserFromToken(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toDTO(user);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setActive(userDTO.isActive());

        userRepo.save(user);
        return UserMapper.toDTO(user);
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public List<UserDTO> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }
}