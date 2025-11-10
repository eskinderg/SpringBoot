package com.project.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.api.auth.CurrentAuthContext;
import com.project.api.core.Constants;
import com.project.api.core.NotFoundException;
import com.project.api.core.SyncConflictException;
import com.project.api.core.utils.JsonHelper;
import com.project.api.core.utils.NoteFactory;
import com.project.api.model.Note;
import com.project.api.repository.NoteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.sql.SQLException;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public List<Map<String, Object>> getNotes() {
//        return this.noteRepository.getUserNotes(CurrentAuthContext.getUserId().toString());

        String sql = "CALL getUserNotes(?)";
        Query query = entityManager.createNativeQuery(sql, Tuple.class);
        query.setParameter(1, CurrentAuthContext.getUserId().toString());
        @SuppressWarnings("unchecked")
        List<Tuple> tupleResults = query.getResultList();
        return tupleResults.stream()
                .map(tuple -> {
                    Map<String, Object> rowMap = new java.util.HashMap<>();
                    // Use getElements() to iterate through columns and aliases
                    for (TupleElement<?> element : tuple.getElements()) {
                        String alias = element.getAlias();
                        Object value = tuple.get(alias);
                        rowMap.put(alias, value);
                    }
                    return rowMap;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Note save(Note note) {
        note.setOwner(CurrentAuthContext.getName());
        note.setUser_id(CurrentAuthContext.getUserId());
        return this.noteRepository.save(note);
    }


//    @Transactional
//    public Note update(Note note) {
//        Note fetchNote = noteRepository.getNote(note.getNote_id(), CurrentAuthContext.getUserId());
//
//        if (fetchNote == null) {
//            throw new NotFoundException("Either the note has been moved or deleted", note);
//        } else {
//            try {
//                return noteRepository.saveAndFlush(note);
//
//            } catch (JpaSystemException ex) {
//
//                SQLException sqlEx = (SQLException) ex.getCause().getCause();
//                String SQL_STATE = sqlEx.getSQLState();
//
//                if (SQL_STATE.equals(Constants.SQL_STATE_CONFLICT))
//                    throw new SyncConflictException("Using old date to update the server", fetchNote);
//
//                if (SQL_STATE.equals(Constants.SQL_NOT_FOUND))
//                    throw new NotFoundException(ex.getMessage(), note);
//            }
//        }
//        return note;
//    }

    @Transactional
    public List<Map<String, Object>> upsert(List<Note> notes) {
        try {
            String notesJson = JsonHelper.convertToJson(notes.stream().map(NoteFactory::create).toList());
//            return noteRepository.note_bulk_upsert(CurrentAuthContext.getUserId().toString(), CurrentAuthContext.getName(), notesJson);

            String sql = "CALL note_bulk_upsert(?,?,?)";
            Query query = entityManager.createNativeQuery(sql, Tuple.class);
            query.setParameter(1, CurrentAuthContext.getUserId().toString());
            query.setParameter(2, CurrentAuthContext.getName());
            query.setParameter(3, notesJson);
            @SuppressWarnings("unchecked")
            List<Tuple> tupleResults = query.getResultList();
            return tupleResults.stream()
                    .map(tuple -> {
                        Map<String, Object> rowMap = new java.util.HashMap<>();
                        // Use getElements() to iterate through columns and aliases
                        for (TupleElement<?> element : tuple.getElements()) {
                            String alias = element.getAlias();
                            Object value = tuple.get(alias);
                            rowMap.put(alias, value);
                        }
                        return rowMap;
                    })
                    .collect(Collectors.toList());
        } catch (JpaSystemException ex) {
            SQLException sqlEx = (SQLException) ex.getCause().getCause();
            String SQL_STATE = sqlEx.getSQLState();

            if (SQL_STATE.equals(Constants.SQL_STATE_CONFLICT))
                throw new SyncConflictException("Using old date to update the server", notes);

            if (SQL_STATE.equals(Constants.SQL_NOT_FOUND))
                throw new NotFoundException(ex.getMessage(), notes);

//            return notes;
            return notes.stream()
                    .map(tuple -> {
                        Map<String, Object> rowMap = new java.util.HashMap<>();
                        // Use getElements() to iterate through columns and aliases
                        return rowMap;
                    })
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public List<Note> bulkInsert(List<Note> notes) throws JsonProcessingException {

        String notesJson = JsonHelper.convertToJson(notes);

        return this.noteRepository.note_bulk_insert(notesJson);
    }
}
