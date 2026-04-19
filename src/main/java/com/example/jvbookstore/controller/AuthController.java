package com.example.jvbookstore.controller;

import com.example.jvbookstore.dto.user.UserRegistrationRequestDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/registration")
    public Object registerUser(@RequestBody UserRegistrationRequestDto requestDto) {
        return null;
    }
}
