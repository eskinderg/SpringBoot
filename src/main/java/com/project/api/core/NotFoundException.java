package com.project.api.core;

import com.project.api.model.Note;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NotFoundException extends RuntimeException {
    private Note note;
    private List<Note> notes;

    public NotFoundException(String msg, Note note) {
        super(msg);
        this.note = note;
    }

    public NotFoundException(String msg, List<Note> notes) {
        super(msg);
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
