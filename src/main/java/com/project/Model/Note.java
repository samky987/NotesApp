package com.project.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "NOTE")
@NoArgsConstructor
@Getter
@Setter
public class Note
{
    //UUID provide better security, but I opted for Long for its performance
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TITLE")
    @NotBlank
    private String title;
    @Column(name = "DESCRIPTION")
    @NotBlank
    private String description;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
