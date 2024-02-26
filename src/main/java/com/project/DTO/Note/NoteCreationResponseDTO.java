package com.project.DTO.Note;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NoteCreationResponseDTO
{
    private Long id;
    private String title;
    private String description;
}