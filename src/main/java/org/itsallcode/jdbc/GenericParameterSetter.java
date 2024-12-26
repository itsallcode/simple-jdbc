package org.itsallcode.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * This {@link PreparedStatement} uses
 * {@link PreparedStatement#setObject(int, Object)} to set parameters from a
 * {@link List}.
 */
class GenericParameterSetter implements PreparedStatementSetter {

    private final List<Object> parameters;

    GenericParameterSetter(final List<Object> parameters) {
        Objects.requireNonNull(parameters, "parameters");
        this.parameters = new ArrayList<>(parameters);
    }

    @Override
    public void setValues(final PreparedStatement preparedStatement) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            preparedStatement.setObject(i + 1, parameters.get(i));
        }
    }
}
