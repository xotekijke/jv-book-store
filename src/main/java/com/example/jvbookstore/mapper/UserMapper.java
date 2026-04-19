package com.example.jvbookstore.mapper;

import com.example.jvbookstore.dto.user.UserRegistrationRequestDto;
import com.example.jvbookstore.dto.user.UserResponseDto;
import com.example.jvbookstore.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    public User toEntity(UserRegistrationRequestDto requestDto) {
        if (requestDto == null) {
            return null;
        }
        
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setShippingAddress(requestDto.getShippingAddress());
        
        return user;
    }
    
    public UserResponseDto toDto(User user) {
        if (user == null) {
            return null;
        }
        
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setEmail(user.getEmail());
        responseDto.setFirstName(user.getFirstName());
        responseDto.setLastName(user.getLastName());
        responseDto.setShippingAddress(user.getShippingAddress());
        
        return responseDto;
    }
}
