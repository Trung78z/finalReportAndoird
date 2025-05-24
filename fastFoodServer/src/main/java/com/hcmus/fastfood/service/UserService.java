package com.hcmus.fastfood.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcmus.fastfood.dto.UserDTO;
import com.hcmus.fastfood.mapper.UserMapper;
import com.hcmus.fastfood.model.User;
import com.hcmus.fastfood.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public UserDTO getUserFromUserName(String username) {
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
        user.setFullName(userDTO.getFullName());
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

    public UserDTO updateCurrentUser(String username, UserDTO userDTO) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setFullName(userDTO.getFullName());
        user.setCity(userDTO.getCity());
        user.setAddress(userDTO.getAddress());

        userRepo.save(user);
        return UserMapper.toDTO(user);
    }
}