package com.project.api.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StoredProcedureService {

    @Autowired
    private  DataSource dataSource;

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> callProcedureForList(String procedureName, Map<String, Object> inParams) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
                .withProcedureName(procedureName)
                .returningResultSet("result_set_name", new GenericMapRowMapper());

        Map<String, Object> result = jdbcCall.execute(inParams); // Execute the stored procedure

        return (List<Map<String, Object>>) result.get("result_set_name");
    }

    public static class GenericMapRowMapper implements RowMapper<Map<String, Object>> {
        @Override
        public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
            }
            return row;
        }
    }
}
