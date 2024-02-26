package com.project.DTO.User;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticationResponseDTO
{
    private String jwt;
}
