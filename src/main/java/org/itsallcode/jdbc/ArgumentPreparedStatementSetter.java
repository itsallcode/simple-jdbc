package org.itsallcode.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

class ArgumentPreparedStatementSetter implements RowPreparedStatementSetter<Object[]> {
    private final ParameterMapper mapper;

    ArgumentPreparedStatementSetter(final ParameterMapper mapper) {
        this.mapper = mapper;
    }

    public void setValues(final Object[] row, final PreparedStatement preparedStatement) throws SQLException {
        int parameterIndex = 1;
        for (final Object arg : row) {
            preparedStatement.setObject(parameterIndex, mapper.map(arg));
            parameterIndex++;
        }
    }
}
