package com.dexwin.notesapp.repository;

import com.dexwin.notesapp.entity.NoteTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteTagRepository extends JpaRepository<NoteTag, Long> {
}
