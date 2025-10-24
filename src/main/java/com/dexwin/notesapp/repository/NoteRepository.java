package com.dexwin.notesapp.repository;

import com.dexwin.notesapp.entity.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    @Query("select n from Note n where n.deletedAt is null and (lower(n.title) like lower(concat('%', :q, '%')) or lower(n.content) like lower(concat('%', :q, '%')))  ")
    Page<Note> search(@Param("q") String q, Pageable pageable);

    // Filter by tags and not deleted
    @Query("SELECT n FROM Note n JOIN n.tags t WHERE n.deletedAt IS NULL AND t.name IN :tags GROUP BY n")
    Page<Note> findByTags(@Param("tags") List<String> tags, Pageable pageable);

    @Query("SELECT n FROM Note n WHERE n.deletedAt IS NULL")
    Page<Note> findAllNotDeleted(Pageable pageable);
}
