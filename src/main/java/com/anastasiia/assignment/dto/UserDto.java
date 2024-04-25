package com.anastasiia.assignment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;

    @Email(message = "Email should be valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    private String firstName;

    private String lastName;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    private String address;

    @Pattern(regexp="(^$|[0-9]{10})", message = "Phone number should be valid")
    private String phoneNumber;
}
