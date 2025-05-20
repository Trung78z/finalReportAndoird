package com.hcmus.fastfood.dto;

import lombok.*;

@Getter
@Setter
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
}