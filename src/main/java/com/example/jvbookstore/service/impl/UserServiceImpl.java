package com.example.jvbookstore.service.impl;

import com.example.jvbookstore.dto.user.UserRegistrationRequestDto;
import com.example.jvbookstore.dto.user.UserResponseDto;
import com.example.jvbookstore.exception.RegistrationException;
import com.example.jvbookstore.mapper.UserMapper;
import com.example.jvbookstore.model.User;
import com.example.jvbookstore.repository.UserRepository;
import com.example.jvbookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User with email " + request.getEmail()
                    + " already exists");
        }

        try {
            User user = userMapper.toEntity(request);

            User savedUser = userRepository.save(user);
            return userMapper.toDto(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new RegistrationException("Could not register user: " + e.getMessage(), e);
        }
    }
}
