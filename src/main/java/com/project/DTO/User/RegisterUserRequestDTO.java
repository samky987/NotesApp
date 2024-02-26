package com.project.DTO.User;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequestDTO
{
    @NotBlank(message = "fullname cannot be empty")
    private String fullName;
    @NotBlank(message = "username cannot be empty")
    private String username;
    @NotBlank(message = "password cannot be empty")
    private String password;
}
