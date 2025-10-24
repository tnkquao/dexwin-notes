package com.dexwin.notesapp.mappers;

import com.dexwin.notesapp.dtos.request.TagRequestDto;
import com.dexwin.notesapp.dtos.response.TagResponseDto;
import com.dexwin.notesapp.entity.Tag;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagResponseDto toDto(Tag note);

    Tag toEntity(TagRequestDto noteRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(TagRequestDto dto, @MappingTarget Tag tag);
}
