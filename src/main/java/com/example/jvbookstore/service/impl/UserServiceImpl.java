package com.example.jvbookstore.service.impl;

import com.example.jvbookstore.dto.user.UserRegistrationRequestDto;
import com.example.jvbookstore.dto.user.UserResponseDto;
import com.example.jvbookstore.exception.RegistrationException;
import com.example.jvbookstore.mapper.UserMapper;
import com.example.jvbookstore.model.Role;
import com.example.jvbookstore.model.User;
import com.example.jvbookstore.repository.RoleRepository;
import com.example.jvbookstore.repository.UserRepository;
import com.example.jvbookstore.service.UserService;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("User with email "
                    + request.getEmail()
                    + " already exists");
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(userRole));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

}
