package com.example.myproject.User.dto;

public record PasswordUpdateDTO(
    String currentPassword,
    String newPassword
) {}
