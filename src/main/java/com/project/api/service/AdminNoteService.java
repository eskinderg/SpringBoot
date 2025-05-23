package com.project.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.auth.CurrentAuthContext;
import com.project.api.core.Constants;
import com.project.api.core.NotFoundException;
import com.project.api.core.SyncConflictException;
import com.project.api.core.utils.JsonHelper;
import com.project.api.model.Note;
import com.project.api.model.User;
import com.project.api.repository.AdminNoteRepository;
import com.project.api.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class AdminNoteService {

    @Autowired
    private AdminNoteRepository adminNoteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public List<Note> getNotes() {
        return this.adminNoteRepository.getAdminNotes();
    }

    @Transactional
    public Note save(Note note) {
        return this.adminNoteRepository.save(note);
    }

//    @Transactional
//    public Note update(Note note) {
//        try {
//            List<Note> singleNoteList = new ArrayList<>();
//            singleNoteList.add(note);
//
//            String noteJson = NoteJsonHelper.convertNotesToJson(singleNoteList);
//            return this.adminNoteRepository.admin_update_note(noteJson);
//        } catch (JpaSystemException ex) {
//
//            SQLException sqlEx = (SQLException) ex.getCause().getCause();
//            String SQL_STATE = sqlEx.getSQLState();
//
//            if (SQL_STATE.equals(Constants.SQL_STATE_CONFLICT))
//                throw new SyncConflictException("Using old date to update the server", note);
//
//            if (SQL_STATE.equals(Constants.SQL_NOT_FOUND))
//                throw new NotFoundException(ex.getMessage(), note);
//        }
//        return null;
//    }

    @Transactional
    public List<Note> bulkUpdate(List<Note> notes) throws JsonProcessingException {
        try {
            String notesJson = JsonHelper.convertToJson(notes);
            return this.adminNoteRepository.admin_bulk_update(notesJson);
        } catch (JpaSystemException ex) {

            SQLException sqlEx = (SQLException) ex.getCause().getCause();
            String SQL_STATE = sqlEx.getSQLState();

            if (SQL_STATE.equals(Constants.SQL_STATE_CONFLICT))
                throw new SyncConflictException("Using old date to update the server", notes);

            if (SQL_STATE.equals(Constants.SQL_NOT_FOUND))
                throw new NotFoundException(ex.getMessage(), notes);
        }
        return null;
    }

    @Transactional
    public List<Note> bulkInsert(List<Note> notes) {
        notes.forEach(note -> {
            note.setOwner(CurrentAuthContext.getName());
            note.setUserId(CurrentAuthContext.getUserId());
        });
        return adminNoteRepository.saveAll(notes);
    }

    @Transactional
    public List<Map<String, Object>> getUsersNotesCount() {
        Query query = entityManager.createNativeQuery("CALL getUsersNotesCount()");
        query.unwrap(org.hibernate.query.NativeQuery.class)
                .setResultTransformer(org.hibernate.transform.AliasToEntityMapResultTransformer.INSTANCE);

        List<Map<String, Object>> result = query.getResultList();
        return result;
    }

    @Transactional
    public List<User> updateUsers(List<User> users) {
        String usersJson = JsonHelper.convertToJson(users);
        return this.userRepository.users_bulk_upsert(usersJson);
    }

    @Transactional
    public List<User> getUsers() {
        return this.userRepository.getUsers();
    }
}
