package com.dexwin.notesapp.controller;

import com.dexwin.notesapp.dtos.request.NoteRequestDto;
import com.dexwin.notesapp.dtos.response.NoteResponseDto;
import com.dexwin.notesapp.entity.Note;
import com.dexwin.notesapp.models.ApiResponseDTO;
import com.dexwin.notesapp.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO> createNote(@Valid @RequestBody final NoteRequestDto noteRequestDto) {
        final NoteResponseDto response = noteService.createNote(noteRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO(true,response,null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> getNote(@PathVariable("id") final Long id) {
        final NoteResponseDto response = noteService.getNoteById(id);
        return ResponseEntity.ok(new ApiResponseDTO(true,response,null));
    }

//    @GetMapping
//    public ResponseEntity<ApiResponseDTO> getNotes() {
//        final List<NoteResponseDto> response = noteService.getNotes();
//        return ResponseEntity.ok(new ApiResponseDTO(true,response,null));
//    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listNotes(@RequestParam(required = false) String q,
                                           @RequestParam(required = false) List<String> tags,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {

        Page<Note> notesPage = noteService.list(q, tags, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("notes", notesPage.getContent());
        response.put("currentPage", notesPage.getNumber());
        response.put("totalItems", notesPage.getTotalElements());
        response.put("totalPages", notesPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> listNotesByUser(@PathVariable("userId") Long userId,
                                                         @RequestParam(required = false) List<String> tags,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {

        Page<Note> notesPage = noteService.getNotesByUser(userId, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("notes", notesPage.getContent());
        response.put("currentPage", notesPage.getNumber());
        response.put("totalItems", notesPage.getTotalElements());
        response.put("totalPages", notesPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseDTO> updateNote(
            @PathVariable final Long id, @Valid @RequestBody final NoteRequestDto requestDto ) {
        final NoteResponseDto response = noteService.updateNote(id, requestDto);
        return ResponseEntity.ok(new ApiResponseDTO(true, response, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteNote(@PathVariable final Long id) {
        noteService.deleteNoteById(id);
//        return ResponseEntity.ok(new ApiResponseDTO(true, "Note with id " + id + " deleted successfully", null));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<NoteResponseDto> restoreNote(@PathVariable Long id, Authentication auth) {
        String username = auth.getName();
        NoteResponseDto restoredNote = noteService.restoreNote(id, username);
        return ResponseEntity.ok(restoredNote);
    }
}
