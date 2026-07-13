package com.example.jvbookstore.security;

import com.example.jvbookstore.dto.user.UserLoginRequestDto;
import com.example.jvbookstore.model.User;
import com.example.jvbookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    public boolean authenticate(UserLoginRequestDto requestDto) {
        Optional<User> user = userRepository.findByEmail(requestDto.email());
        return user.isPresent() && user.get().getPassword().equals(requestDto.password());
    }
}
