package com.project.api.core.services;

import com.project.api.core.Constants;
import com.project.api.core.NotFoundException;
import com.project.api.core.SyncConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class StoredProcedureService {

    @Autowired
    private  DataSource dataSource;

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> callProcedureForList(String procedureName, Map<String, Object> inParams) throws SyncConflictException,NotFoundException {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
                .withProcedureName(procedureName)
                .returningResultSet("result_set_name", new ColumnMapRowMapper());

        Map<String, Object> result = Map.of();

        try {
            result = jdbcCall.execute(inParams); // Execute the stored procedure
        } catch (UncategorizedSQLException ex) {
            SQLException sqlEx = ex.getSQLException();
            assert sqlEx != null;
            String SQL_STATE = sqlEx.getSQLState();

            switch (SQL_STATE){
                case Constants.SQL_STATE_CONFLICT:
                    throw new SyncConflictException("Executing stored procedure", ex.getSql(), ex.getSQLException());
                case Constants.SQL_NOT_FOUND:
                    throw new NotFoundException(ex.getMessage());
            }
        }
        return (List<Map<String, Object>>) result.get("result_set_name");
    }
}
