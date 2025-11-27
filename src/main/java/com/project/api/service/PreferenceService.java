package com.project.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.api.auth.CurrentAuthContext;
import com.project.api.core.Constants;
import com.project.api.core.NotFoundException;
import com.project.api.core.SyncConflictException;
import com.project.api.core.utils.JsonHelper;
import com.project.api.core.utils.NoteFactory;
import com.project.api.model.Note;
import com.project.api.model.Preference;
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
public class PreferenceService {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public List<Map<String, Object>> getUserPreference() {
//        return this.noteRepository.getUserNotes(CurrentAuthContext.getUserId().toString());

        String sql = "CALL getUserPreference(?)";
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
    public List<Map<String, Object>> upsert(List<Preference> preferences) {
        try {
            String preferencesJson = JsonHelper.convertToJson(preferences.stream().toList());

            String sql = "CALL preference_bulk_upsert(?,?)";
            Query query = entityManager.createNativeQuery(sql, Tuple.class);
            query.setParameter(1, CurrentAuthContext.getUserId().toString());
            query.setParameter(2, preferencesJson);
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

            return preferences.stream()
                    .map(tuple -> {
                        Map<String, Object> rowMap = new java.util.HashMap<>();
                        // Use getElements() to iterate through columns and aliases
                        return rowMap;
                    })
                    .collect(Collectors.toList());
        }
    }
}
