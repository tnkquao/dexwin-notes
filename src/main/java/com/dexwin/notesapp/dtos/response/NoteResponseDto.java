package com.dexwin.notesapp.dtos.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
public class NoteResponseDto {
    private Long id;

    private UserResponseDto user;

    private String title;

    private String content;

    private Set<TagResponseDto> tags;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
