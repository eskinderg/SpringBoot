package com.project.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.auth.CurrentAuthContext;
import com.project.api.core.services.StoredProcedureService;
import com.project.api.core.utils.JsonHelper;
import com.project.api.core.utils.NoteFactory;
import com.project.api.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class NoteService {

    @Autowired
    private StoredProcedureService spService;

    @Transactional
    public List<Map<String, Object>> getNotes() {
        Map<String, Object> params = Map.of("p_user_id", CurrentAuthContext.getUserId().toString());
        return spService.callProcedureForList("getUserNotes", params);
    }

    @Transactional
    public List<Map<String, Object>> getNoteHistory(Note note) {
        Map<String, Object> params = Map.of(
                "p_user_id", CurrentAuthContext.getUserId().toString(),
                "p_note_id", note.getNote_id().toString());
        return spService.callProcedureForList("getNoteHistory", params);
    }

    @Transactional
    public List<Map<String, Object>> upsert(List<Note> notes) {
        if (CurrentAuthContext.hasRole("Write")) {
                String notesJson = JsonHelper.convertToJson(notes.stream().map(NoteFactory::create).toList());
                Map<String, Object> params = Map.of(
                        "p_user_id", CurrentAuthContext.getUserId().toString(),
                        "p_owner", CurrentAuthContext.getName(),
                        "notes_json", notesJson);
                return spService.callProcedureForList("note_bulk_upsert", params);
        } else {

            return notes.stream()
                    .map(note -> new ObjectMapper().convertValue(note, new TypeReference<Map<String, Object>>() {
                    }))
                    .toList();
        }
    }

    @Transactional
    public List<Map<String, Object>> bulkInsert(List<Note> notes) throws JsonProcessingException {
        String notesJson = JsonHelper.convertToJson(notes);
        Map<String, Object> params = Map.of("notes_json", notesJson);
        return spService.callProcedureForList("note_bulk_insert", params);
    }
}
