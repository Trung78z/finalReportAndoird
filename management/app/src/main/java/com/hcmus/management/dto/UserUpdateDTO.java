package com.hcmus.management.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String email;
    private String phoneNumber;
    private String fullName;
    private String address;
    private String city;
}
