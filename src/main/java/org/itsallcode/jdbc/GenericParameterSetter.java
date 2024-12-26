package org.itsallcode.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This {@link PreparedStatement} uses
 * {@link PreparedStatement#setObject(int, Object)} to set parameters from a
 * {@link List}.
 */
class GenericParameterSetter implements PreparedStatementSetter {

    private final List<Object> values;

    GenericParameterSetter(final List<Object> values) {
        this.values = new ArrayList<>(values);
    }

    @Override
    public void setValues(final PreparedStatement preparedStatement) throws SQLException {
        for (int i = 0; i < values.size(); i++) {
            preparedStatement.setObject(i + 1, values.get(i));
        }
    }
}
