package com.example.jvbookstore.dto.user;

import com.example.jvbookstore.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldMatch(field = "password", fieldMatch = "repeatPassword", message = "Passwords do not match")
public class UserRegistrationRequestDto {

    @NotBlank
    @Email
    @Size
    private String email;

    @NotBlank
    @Size
    private String password;

    @NotBlank
    private String repeatPassword;

    @NotBlank
    @Size(max = 30, message = "First name must be less than 100 characters")
    private String firstName;

    @NotBlank
    @Size(max = 30, message = "Last name must be less than 100 characters")
    private String lastName;

    @Size(max = 500, message = "Shipping address must be less than 500 characters")
    private String shippingAddress;
}

