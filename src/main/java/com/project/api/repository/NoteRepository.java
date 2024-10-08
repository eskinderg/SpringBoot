package com.project.api.repository;

import com.project.api.model.Note;
import com.project.api.model.NoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, NoteId> {
    @Query("select n from Note n where n.userId = ?1 ORDER BY n.dateModified DESC")
    List<Note> getNotes(UUID id);

    @Query("select n from Note n where n.id = ?1 AND n.userId = ?2")
    Note getNote(UUID noteId, UUID userId);
}
