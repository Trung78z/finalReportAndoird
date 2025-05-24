package com.hcmus.fastfood.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String username;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private boolean isActive;
    private UserRoleDTO role;
}