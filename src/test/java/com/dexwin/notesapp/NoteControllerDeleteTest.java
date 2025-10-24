package com.dexwin.notesapp;

import com.dexwin.notesapp.controller.NoteController;
import com.dexwin.notesapp.service.NoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
public class NoteControllerDeleteTest {
    @Autowired
    private MockMvc mockMvc;

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
    @DisplayName("DELETE /api/notes/{id} - Soft Delete Success")
    @WithMockUser(username = "tarek", roles = {"USER"})
    void deleteNote_success() throws Exception {
        Mockito.doNothing().when(noteService).deleteNoteById(anyLong());

        mockMvc.perform(delete("/api/notes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/notes/{id} - Not Found")
    @WithMockUser(username = "tarek", roles = {"USER"})
    void deleteNote_notFound() throws Exception {
        Mockito.doThrow(new RuntimeException("Note not found")).when(noteService).deleteNoteById(anyLong());

        mockMvc.perform(delete("/api/notes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Note not found"));
    }
}
