package com.dexwin.notesapp.controller;

import com.dexwin.notesapp.dtos.response.UserResponseDto;
import com.dexwin.notesapp.models.ApiResponseDTO;
import com.dexwin.notesapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getUser(@PathVariable("id") final Long id) {
        final UserResponseDto response = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponseDTO(true,response,null));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO> getUsers() {
        final List<UserResponseDto> response = userService.getUsers();
        return ResponseEntity.ok(new ApiResponseDTO(true,response,null));
    }
}
