package com.project.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.auth.CurrentAuthContext;
import com.project.api.core.Constants;
import com.project.api.core.NotFoundException;
import com.project.api.core.SyncConflictException;
import com.project.api.core.utils.JsonHelper;
import com.project.api.core.utils.NoteFactory;
import com.project.api.model.Note;
import com.project.api.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public List<Map<String, Object>> getNotes() {
        String sql = "{CALL getUserNotes(?)}";
        return jdbcTemplate.queryForList(sql, CurrentAuthContext.getUserId().toString());
    }

    @Transactional
    public Note save(Note note) {
        note.setOwner(CurrentAuthContext.getName());
        note.setUser_id(CurrentAuthContext.getUserId());
        return this.noteRepository.save(note);
    }

    @Transactional
    public List<Map<String, Object>> upsert(List<Note> notes) {
        if (CurrentAuthContext.hasRole("Write")) {
            try {
                String notesJson = JsonHelper.convertToJson(notes.stream().map(NoteFactory::create).toList());
                String sql = "{CALL note_bulk_upsert(?, ?, ?)}";
                return jdbcTemplate.queryForList(sql, CurrentAuthContext.getUserId().toString(), CurrentAuthContext.getName(), notesJson);
            } catch (JpaSystemException ex) {
                SQLException sqlEx = (SQLException) ex.getCause().getCause();
                String SQL_STATE = sqlEx.getSQLState();

                if (SQL_STATE.equals(Constants.SQL_STATE_CONFLICT))
                    throw new SyncConflictException("Using old date to update the server", notes);

                if (SQL_STATE.equals(Constants.SQL_NOT_FOUND))
                    throw new NotFoundException(ex.getMessage(), notes);

                return notes.stream()
                        .map(note -> new ObjectMapper().convertValue(note, new TypeReference<Map<String, Object>>() {
                        }))
                        .toList();
            }
        } else {

            return notes.stream()
                    .map(note -> new ObjectMapper().convertValue(note, new TypeReference<Map<String, Object>>() {
                    }))
                    .toList();
        }
    }

    @Transactional
    public List<Note> bulkInsert(List<Note> notes) throws JsonProcessingException {
        String notesJson = JsonHelper.convertToJson(notes);
        return this.noteRepository.note_bulk_insert(notesJson);
    }
}
