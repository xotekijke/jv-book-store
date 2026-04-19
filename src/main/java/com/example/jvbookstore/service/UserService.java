package com.example.jvbookstore.service;

import com.example.jvbookstore.dto.user.UserRegistrationRequestDto;
import com.example.jvbookstore.dto.user.UserResponseDto;
import com.example.jvbookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request) throws RegistrationException;
}
