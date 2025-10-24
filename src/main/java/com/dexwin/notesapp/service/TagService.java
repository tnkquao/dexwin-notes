package com.dexwin.notesapp.service;

import com.dexwin.notesapp.dtos.request.TagRequestDto;
import com.dexwin.notesapp.dtos.response.TagResponseDto;
import com.dexwin.notesapp.entity.Tag;
import com.dexwin.notesapp.mappers.TagMapper;
import com.dexwin.notesapp.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagResponseDto createTag(TagRequestDto requestDto) {
        if (requestDto == null)
            throw new IllegalArgumentException("Tag requestDto is null");

        Tag entity = tagMapper.toEntity(requestDto);

        String lowered = entity.getName().trim().toLowerCase();
        entity.setName(lowered);

        Tag saved = tagRepository.save(entity);

        return tagMapper.toDto(saved);
    }

    public TagResponseDto getTagById(Long id) {
        return tagRepository.findById(id)
                .map(tagMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("Tag not found"));
    }

    public List<TagResponseDto> getTags() {
        return tagRepository.findAll()
                .stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());
    }

    public TagResponseDto updateTag(Long id, TagRequestDto requestDto) {
        if (requestDto == null) {
            throw new IllegalArgumentException("Tag RequestDto is null");
        }

        final Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tag not found"));
        tagMapper.updateEntityFromDto(requestDto, tag);

        Tag updated = tagRepository.save(tag);

        return tagMapper.toDto(updated);
    }

    public void deleteTagById(Long id) {
        if (!tagRepository.existsById(id))
            throw new NoSuchElementException("Tag with id " + id + " not found");
        tagRepository.deleteById(id);
    }
}
