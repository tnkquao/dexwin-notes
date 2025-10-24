package com.dexwin.notesapp.dtos.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TagResponseDto {
    private Long id;

    private String name;

    private String description;
}
