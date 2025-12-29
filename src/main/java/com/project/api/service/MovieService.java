package com.project.api.service;

import com.project.api.auth.CurrentAuthContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class MovieService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public List<Map<String, Object>> getUserMovies() {
        String sql = "{CALL getUserMovies(?)}";
        return jdbcTemplate.queryForList(sql, CurrentAuthContext.getUserId().toString());
    }

    @Transactional
    public List<Map<String, Object>> getWatchedUserMovies() {
        String sql = "{CALL getUserWatchedMovies(?)}";
        return jdbcTemplate.queryForList(sql, CurrentAuthContext.getUserId().toString());
    }

    @Transactional
    public List<Map<String, Object>> movieBulkUpsert(String movies) {
        String sql = "{CALL movies_bulk_upsert(?)}";
        return jdbcTemplate.queryForList(sql, movies);
    }

    @Transactional
    public List<Map<String, Object>> watchedBulkUpsert(String movies) {
        String sql = "{CALL watched_movies_upsert(?)}";
        return jdbcTemplate.queryForList(sql, movies);
    }

    @Transactional
    public List<Map<String, Object>> populateMovies(String movies) {
        String sql = "{CALL populate_movies(?)}";
        return jdbcTemplate.queryForList(sql, movies);
    }
}
