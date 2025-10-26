package com.dexwin.notesapp.controller;

import com.dexwin.notesapp.dtos.response.UserResponseDto;
import com.dexwin.notesapp.models.ApiResponseDTO;
import com.dexwin.notesapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for retrieving user data")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getUser(@PathVariable("id") final Long id) {
        final UserResponseDto response = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponseDTO(true,response,null));
    }

    @Operation(summary = "List all users")
    @GetMapping
    public ResponseEntity<ApiResponseDTO> getUsers() {
        final List<UserResponseDto> response = userService.getUsers();
        return ResponseEntity.ok(new ApiResponseDTO(true,response,null));
    }
}
