package com.dexwin.notesapp;

import com.dexwin.notesapp.controller.NoteController;
import com.dexwin.notesapp.dtos.request.NoteRequestDto;
import com.dexwin.notesapp.dtos.response.NoteResponseDto;
import com.dexwin.notesapp.dtos.response.TagResponseDto;
import com.dexwin.notesapp.entity.Note;
import com.dexwin.notesapp.entity.Tag;
import com.dexwin.notesapp.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NoteService noteService;

    @TestConfiguration
    static class Config {
        @Bean
        public NoteService noteService() {
            return Mockito.mock(NoteService.class);
        }
    }

    @Test
    @DisplayName("POST /api/notes - Success")
    @WithMockUser(username = "tarek", roles = {"USER"})
    void createNote_success() throws Exception {
        NoteRequestDto request = new NoteRequestDto();
        request.setTitle("Test Note");
        request.setContent("This is a test");
        request.setTags(Set.of("tag1","tag2"));

        NoteResponseDto response = new NoteResponseDto();
        response.setId(1L);
        response.setTitle(request.getTitle());
        response.setContent(request.getContent());
        // response.setTags(request.getTags());
        response.setCreatedAt(Timestamp.from(Instant.now()));
        response.setUpdatedAt(Timestamp.from(Instant.now()));

        Mockito.when(noteService.createNote(any(NoteRequestDto.class), "tarek")).thenReturn(response);

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Note"))
                .andExpect(jsonPath("$.tags[0]").value("tag1"));
    }

    @Test
    @DisplayName("POST /api/notes - Validation Error")
    @WithMockUser(username = "tarek", roles = {"USER"})
    void createNote_validationError() throws Exception {
        NoteRequestDto request = new NoteRequestDto(); // missing title/content

        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @DisplayName("GET /api/notes - List Notes")
    @WithMockUser(username = "tarek", roles = {"USER"})
    void listNotes_success() throws Exception {
        TagResponseDto tag1 = new TagResponseDto();
        tag1.setName("science");

        NoteResponseDto note = new NoteResponseDto();
        note.setId(1L);
        note.setTitle("Test Note");
        note.setContent("Content");
        note.setTags(Set.of(tag1));
        note.setCreatedAt(Timestamp.from(Instant.now()));
        note.setUpdatedAt(Timestamp.from(Instant.now()));

        Page<NoteResponseDto> notePage = new PageImpl<>(List.of(note), PageRequest.of(0, 10), 1);

        Mockito.when(noteService.list(any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(note), PageRequest.of(0, 10), 1));

        mockMvc.perform(get("/api/notes")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "updatedAt,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Note"));
    }
}