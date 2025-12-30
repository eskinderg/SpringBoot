package com.project.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.api.auth.CurrentAuthContext;
import com.project.api.core.services.StoredProcedureService;
import com.project.api.core.utils.JsonHelper;
import com.project.api.model.Preference;
import com.project.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class PreferenceService {

    @Autowired
    private StoredProcedureService spService;

    @Transactional
    public List<Map<String, Object>> getUserPreference() {
        Map<String, Object> params = Map.of("p_user_id", CurrentAuthContext.getUserId().toString());
        return spService.callProcedureForList("getUserPreference", params);
    }

    @Transactional
    public List<Map<String, Object>> upsert(List<Preference> preferences) {
        try {
            String preferencesJson = JsonHelper.convertToJson(preferences.stream().toList());
            Map<String, Object> params = Map.of(
                    "p_user_id", CurrentAuthContext.getUserId().toString(),
                    "preferences_json", preferencesJson);
            return spService.callProcedureForList("preference_bulk_upsert", params);
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
            Map<String, Object> params = Map.of(
                    "p_user_id", CurrentAuthContext.getUserId().toString(),
                    "users_json", usersJson);
            return spService.callProcedureForList("user_info_upsert", params);

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
        Map<String, Object> params = Map.of(
                "p_user_id", CurrentAuthContext.getUserId().toString(),
                "p_owner", CurrentAuthContext.getName(),
                "p_firstname", CurrentAuthContext.getName(),
                "p_email", CurrentAuthContext.getUserEmail(),
                "p_created_at", ""
                );
        return spService.callProcedureForList("getUserInfo", params);
    }
}
