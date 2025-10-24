package com.dexwin.notesapp.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class NoteRequestDto {
    @NotNull(message = "User id is required")
    private Long userId;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title too long")
    private String title;

    @NotBlank(message = "Content cannot be blank")
    private String content;

    private List<String> tags;
}
