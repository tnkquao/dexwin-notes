package com.dexwin.notesapp.dtos.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TagRequestDto {
    private String name;

    private String description;
}
