package com.project.DTO.Error;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO
{
    private String field;
    private String message;
}
