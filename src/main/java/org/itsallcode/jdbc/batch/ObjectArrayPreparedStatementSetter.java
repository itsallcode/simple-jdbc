package org.itsallcode.jdbc.batch;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.itsallcode.jdbc.RowPreparedStatementSetter;

class ObjectArrayPreparedStatementSetter implements RowPreparedStatementSetter<Object[]> {
    public void setValues(final Object[] row, final PreparedStatement preparedStatement) throws SQLException {
        int parameterIndex = 1;
        for (final Object arg : row) {
            preparedStatement.setObject(parameterIndex, arg);
            parameterIndex++;
        }
    }
}
