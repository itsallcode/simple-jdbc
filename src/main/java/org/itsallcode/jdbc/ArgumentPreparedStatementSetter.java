package org.itsallcode.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

class ArgumentPreparedStatementSetter implements PreparedStatementSetter {
    private final Object[] args;
    private final ParameterMapper mapper;

    ArgumentPreparedStatementSetter(final ParameterMapper mapper, final Object[] args) {
        this.mapper = mapper;
        this.args = args;
    }

    public void setValues(final PreparedStatement preparedStatement) throws SQLException {
        int parameterIndex = 1;
        for (final Object arg : args) {
            preparedStatement.setObject(parameterIndex, mapper.map(arg));
            parameterIndex++;
        }
    }
}
