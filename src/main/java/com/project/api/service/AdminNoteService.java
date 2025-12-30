package com.project.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.api.core.Constants;
import com.project.api.core.NotFoundException;
import com.project.api.core.SyncConflictException;
import com.project.api.core.services.StoredProcedureService;
import com.project.api.core.utils.JsonHelper;
import com.project.api.model.Note;
import com.project.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class AdminNoteService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StoredProcedureService spService;

    @Transactional
    public List<Map<String, Object>> getNotes() {
        return spService.callProcedureForList("getAdminNotes", Map.of());
    }

    @Transactional
    public List<Map<String, Object>> bulkUpdate(List<Note> notes) throws JsonProcessingException {
        try {
            String notesJson = JsonHelper.convertToJson(notes);
            Map<String, Object> params = Map.of("notes_json", notesJson);
            return spService.callProcedureForList("admin_bulk_update", params);

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
    public List<Map<String, Object>> getUsersNotesCount() {
        return spService.callProcedureForList("getUsersNotesCount", Map.of());
    }

    @Transactional
    public List<Map<String, Object>> updateUsers(List<User> users) {
        String usersJson = JsonHelper.convertToJson(users);
        Map<String, Object> params = Map.of("users_json", usersJson);
        return spService.callProcedureForList("users_bulk_upsert", params);
    }

    @Transactional
    public List<Map<String, Object>> getUsers() {
        return spService.callProcedureForList("getUsers", Map.of());
    }
}
