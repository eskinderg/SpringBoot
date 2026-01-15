package com.project.api.core;

import com.project.api.model.Note;
import org.springframework.jdbc.UncategorizedSQLException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SyncConflictException extends UncategorizedSQLException {
    private Note note;
    private List<Note> notes;

    public SyncConflictException(String task, String sql, SQLException ex) {
        super(task,sql, ex);
    }

    public SyncConflictException(String task, String sql, SQLException ex, Note note) {
        super(task,sql, ex);
        this.note = note;
    }

    public SyncConflictException(String task, String sql, SQLException ex, List<Note> notes) {
        super(task, sql, ex);
        this.notes = notes;
    }

    public Note getNote() {
        return this.note;
    }

    public List<Map<String, Object>> getNotes() {
//        return this.notes;
        return this.notes.stream()
                .map(tuple -> {
                    Map<String, Object> rowMap = new java.util.HashMap<>();
                    // Use getElements() to iterate through columns and aliases
                    return rowMap;
                })
                .collect(Collectors.toList());
    }
}
