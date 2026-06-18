package com.example.bookmanagement.dto;


import lombok.Data;

@Data
public class ForgotPasswordDTO {

    private String username;

    private String newPassword;

    private String confirmPassword;
}