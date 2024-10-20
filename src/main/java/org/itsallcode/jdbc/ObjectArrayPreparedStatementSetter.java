package org.itsallcode.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

class ObjectArrayPreparedStatementSetter implements RowPreparedStatementSetter<Object[]> {
    public void setValues(final Object[] row, final PreparedStatement preparedStatement) throws SQLException {
        int parameterIndex = 1;
        for (final Object arg : row) {
            preparedStatement.setObject(parameterIndex, arg);
            parameterIndex++;
        }
    }
}
