package com.project.api.service;

import com.project.api.auth.CurrentAuthContext;
import com.project.api.core.services.StoredProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class MovieService {

    @Autowired
    private StoredProcedureService spService;

    @Transactional
    public List<Map<String, Object>> getUserMovies() {
        Map<String, Object> params = Map.of("p_user_id", CurrentAuthContext.getUserId().toString());
        return spService.callProcedureForList("getUserMovies", params);
    }

    @Transactional
    public List<Map<String, Object>> getWatchedUserMovies() {
        Map<String, Object> params = Map.of("p_user_id", CurrentAuthContext.getUserId().toString());
        return spService.callProcedureForList("getUserWatchedMovies", params);
    }

    @Transactional
    public List<Map<String, Object>> movieBulkUpsert(String movies) {
        Map<String, Object> params = Map.of("movie_json", movies);
        return spService.callProcedureForList("movies_bulk_upsert", params);
    }

    @Transactional
    public List<Map<String, Object>> watchedBulkUpsert(String movies) {
        Map<String, Object> params = Map.of("movie_json", movies);
        return spService.callProcedureForList("watched_movies_upsert", params);
    }

    @Transactional
    public List<Map<String, Object>> populateMovies(String movies) {
        Map<String, Object> params = Map.of("movie_json", movies);
        return spService.callProcedureForList("populate_movies", params);
    }
}
