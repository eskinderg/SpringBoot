package com.project.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.auth.CurrentAuthContext;
import com.project.api.core.utils.JsonHelper;
import com.project.api.model.Preference;
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
public class PreferenceService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public List<Map<String, Object>> getUserPreference() {

        String sql = "{CALL getUserPreference(?)}";
        return jdbcTemplate.queryForList(sql, CurrentAuthContext.getUserId().toString());
    }

    @Transactional
    public List<Map<String, Object>> upsert(List<Preference> preferences) {
        try {
            String preferencesJson = JsonHelper.convertToJson(preferences.stream().toList());
            String sql = "{CALL preference_bulk_upsert(?,?)}";
            return jdbcTemplate.queryForList(sql, CurrentAuthContext.getUserId().toString(), preferencesJson);
        } catch (JpaSystemException ex) {
            SQLException sqlEx = (SQLException) ex.getCause().getCause();
            String SQL_STATE = sqlEx.getSQLState();

            return preferences.stream()
                    .map(note -> new ObjectMapper().convertValue(note, new TypeReference<Map<String, Object>>() {
                    }))
                    .toList();
        }
    }

    @Transactional
    public List<Map<String, Object>> upsertUserInfo(List<User> users) {
        try {
            String usersJson = JsonHelper.convertToJson(users.stream().toList());
            String sql = "{CALL user_info_upsert(?,?)}";
            return jdbcTemplate.queryForList(sql, CurrentAuthContext.getUserId().toString(), usersJson);

        } catch (JpaSystemException ex) {
            SQLException sqlEx = (SQLException) ex.getCause().getCause();
            String SQL_STATE = sqlEx.getSQLState();

            return users.stream()
                    .map(note -> new ObjectMapper().convertValue(note, new TypeReference<Map<String, Object>>() {
                    }))
                    .toList();
        }
    }

    @Transactional
    public List<Map<String, Object>> getUseInfo() {
        String sql = "{CALL getUserInfo(?,?,?,?,?)}";
        return jdbcTemplate.queryForList(sql, CurrentAuthContext.getUserId().toString(), CurrentAuthContext.getName(), CurrentAuthContext.getName(), CurrentAuthContext.getUserEmail(), " ");
    }
}
