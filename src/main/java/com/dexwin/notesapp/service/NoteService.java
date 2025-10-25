package com.dexwin.notesapp.service;

import com.dexwin.notesapp.dtos.request.NoteRequestDto;
import com.dexwin.notesapp.dtos.response.NoteResponseDto;
import com.dexwin.notesapp.entity.Note;
import com.dexwin.notesapp.entity.Tag;
import com.dexwin.notesapp.mappers.NoteMapper;
import com.dexwin.notesapp.repository.NoteRepository;
import com.dexwin.notesapp.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final TagRepository tagRepository;

    public NoteResponseDto createNote(NoteRequestDto requestDto) {
        if (requestDto == null)
            throw new IllegalArgumentException("Note requestDto is null");

        Note entity = noteMapper.toEntity(requestDto);
        entity.setTags(resolveTags(requestDto.getTags()));

        Note saved = noteRepository.save(entity);

        return noteMapper.toDto(saved);
    }

    public NoteResponseDto getNoteById(Long id) {
        return noteRepository.findById(id)
                .map(noteMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Note not found"));
    }

    public List<NoteResponseDto> getNotes() {
        return noteRepository.findAll()
                .stream()
                .map(noteMapper::toDto)
                .collect(Collectors.toList());
    }

    public Page<Note> getNotesByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));

        return noteRepository.findActiveNotesByUserId(userId, pageable);

    }

    public Page<Note> list(String q, List<String> tags, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));

        if (q != null && !q.isBlank()) {
            return noteRepository.search(q, pageable);
        }

        if (tags != null && !tags.isEmpty()) {
            return noteRepository.findByTags(tags, pageable);
        }

        return noteRepository.findAllNotDeleted(pageable);
    }

    public NoteResponseDto updateNote(Long id, NoteRequestDto requestDto) {
        if (requestDto == null) {
            throw new IllegalArgumentException("Note RequestDto is null");
        }

        final Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Note not found"));
        noteMapper.updateEntityFromDto(requestDto, note);

        Note updated = noteRepository.save(note);

        return noteMapper.toDto(updated);
    }

    @Transactional
    public void deleteNoteById(Long id) {
        if (!noteRepository.existsById(id))
            throw new NoSuchElementException("Note with id " + id + " not found");
        noteRepository.findById(id).ifPresent(n -> {
            n.setDeletedAt(Timestamp.from(Instant.now()));
            noteRepository.save(n);
        });
    }

    public NoteResponseDto restoreNote(Long notedId, String username) {
        Note note = noteRepository.findById(notedId)
                .orElseThrow( () -> new NoSuchElementException("Note with id " + notedId + " not found"));

        // Check ownership
        if (!note.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not allowed to restore this note");
        }

        // Check if it was deleted
        if (note.getDeletedAt() == null) {
            throw new IllegalStateException("Note is not deleted");
        }

        note.setDeletedAt(null);
        Note restored = noteRepository.save(note);

        return noteMapper.toDto(restored);
    }

    private Set<Tag> resolveTags(List<String> tagNames) {
        if (tagNames == null) return new HashSet<>();
        return tagNames.stream()
                .map(name -> tagRepository.findByName(name).orElseGet(() -> tagRepository.save(new Tag() {{ setName(name); }})))
                .collect(Collectors.toSet());
    }
}
