package com.dexwin.notesapp.controller;

import com.dexwin.notesapp.dtos.request.NoteRequestDto;
import com.dexwin.notesapp.dtos.response.NoteResponseDto;
import com.dexwin.notesapp.entity.Note;
import com.dexwin.notesapp.models.ApiResponseDTO;
import com.dexwin.notesapp.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
@Tag(name = "Notes", description = "Endpoints for managing notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @Operation(summary = "Create a new note", responses = {
            @ApiResponse(responseCode = "201", description = "Note created successfully")
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO> createNote(@Valid @RequestBody final NoteRequestDto noteRequestDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        final NoteResponseDto response = noteService.createNote(noteRequestDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO(true,response,null));
    }

    @Operation(summary = "Get note by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Note found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getNote(@PathVariable("id") final Long id) {
        final NoteResponseDto response = noteService.getNoteById(id);
        return ResponseEntity.ok(new ApiResponseDTO(true,response,null));
    }

    @Operation(summary = "List all notes", description = "Filter by query or tags with pagination")
    @GetMapping
    public ResponseEntity<Map<String, Object>> listNotes(@RequestParam(required = false) String q,
                                           @RequestParam(required = false) List<String> tags,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {

        Page<NoteResponseDto> notesPage = noteService.list(q, tags, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("content", notesPage.getContent());
        response.put("currentPage", notesPage.getNumber());
        response.put("totalItems", notesPage.getTotalElements());
        response.put("totalPages", notesPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List notes by user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> listNotesByUser(@PathVariable("userId") Long userId,
                                                         @RequestParam(required = false) List<String> tags,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Page<Note> notesPage = noteService.getNotesByUser(userId, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("notes", notesPage.getContent());
        response.put("currentPage", notesPage.getNumber());
        response.put("totalItems", notesPage.getTotalElements());
        response.put("totalPages", notesPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update note by ID")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateNote(
            @PathVariable final Long id, @Valid @RequestBody final NoteRequestDto requestDto ) {
        final NoteResponseDto response = noteService.updateNote(id, requestDto);
        return ResponseEntity.ok(new ApiResponseDTO(true, response, null));
    }

    @Operation(summary = "Soft delete note by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteNote(@PathVariable final Long id) {
        noteService.deleteNoteById(id);
//        return ResponseEntity.ok(new ApiResponseDTO(true, "Note with id " + id + " deleted successfully", null));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Restore a soft-deleted note")
    @PutMapping("/{id}/restore")
    public ResponseEntity<NoteResponseDto> restoreNote(@PathVariable Long id, Authentication auth) {
        String username = auth.getName();
        NoteResponseDto restoredNote = noteService.restoreNote(id, username);
        return ResponseEntity.ok(restoredNote);
    }
}
