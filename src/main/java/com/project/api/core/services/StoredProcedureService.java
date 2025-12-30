package com.project.api.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
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
                .returningResultSet("result_set_name", new ColumnMapRowMapper());

        Map<String, Object> result = jdbcCall.execute(inParams); // Execute the stored procedure

        return (List<Map<String, Object>>) result.get("result_set_name");
    }
}
