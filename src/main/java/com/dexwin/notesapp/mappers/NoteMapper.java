package com.dexwin.notesapp.mappers;

import com.dexwin.notesapp.dtos.request.NoteRequestDto;
import com.dexwin.notesapp.dtos.response.NoteResponseDto;
import com.dexwin.notesapp.entity.Note;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    NoteResponseDto toDto(Note note);
    @Mapping(target = "tags", ignore = true)
    Note toEntity(NoteRequestDto noteRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tags", ignore = true)
    void updateEntityFromDto(NoteRequestDto dto, @MappingTarget Note note);
}
