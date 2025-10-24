package com.dexwin.notesapp.controller;

import com.dexwin.notesapp.dtos.request.NoteRequestDto;
import com.dexwin.notesapp.dtos.request.TagRequestDto;
import com.dexwin.notesapp.dtos.response.NoteResponseDto;
import com.dexwin.notesapp.dtos.response.TagResponseDto;
import com.dexwin.notesapp.entity.Note;
import com.dexwin.notesapp.models.ApiResponseDTO;
import com.dexwin.notesapp.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO> createTag(@Valid @RequestBody final TagRequestDto tagRequestDto) {
        final TagResponseDto response = tagService.createTag(tagRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO(true,response,null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getTag(@PathVariable("id") final Long id) {
        final TagResponseDto response = tagService.getTagById(id);
        return ResponseEntity.ok(new ApiResponseDTO(true,response,null));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO> getTags() {
        final List<TagResponseDto> response = tagService.getTags();
        return ResponseEntity.ok(new ApiResponseDTO(true,response,null));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateUser(
            @PathVariable final Long id, @Valid @RequestBody final TagRequestDto requestDto ) {
        final TagResponseDto response = tagService.updateTag(id, requestDto);
        return ResponseEntity.ok(new ApiResponseDTO(true, response, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> deleteTag(@PathVariable final Long id) {
        tagService.deleteTagById(id);
        return ResponseEntity.ok(new ApiResponseDTO(true, "Note with id " + id + " deleted successfully", null));
    }
}
