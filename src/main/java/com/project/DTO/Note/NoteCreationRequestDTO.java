package com.project.DTO.Note;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NoteCreationRequestDTO
{
    @NotBlank(message = "Title cannot be empty")
    private String title;
    @NotBlank(message = "description cannot be empty")
    private String description;
}
