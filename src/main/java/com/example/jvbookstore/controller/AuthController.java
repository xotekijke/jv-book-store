package com.example.jvbookstore.controller;

import com.example.jvbookstore.dto.user.UserRegistrationRequestDto;
import com.example.jvbookstore.dto.user.UserResponseDto;
import com.example.jvbookstore.exception.RegistrationException;
import com.example.jvbookstore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and authentication")
public class AuthController {
    private final UserService userService;

    @PostMapping("/registration")
    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account and returns the registered user's details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid request body / validation failed"),
            @ApiResponse(responseCode = "409",
                    description = "Registration failed, e.g. email already in use")
    })
    public UserResponseDto registerUser(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }
}
