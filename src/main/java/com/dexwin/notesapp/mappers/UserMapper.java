package com.dexwin.notesapp.mappers;

import com.dexwin.notesapp.dtos.response.UserResponseDto;
import com.dexwin.notesapp.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toDto(User note);
}
