package org.itsallcode.jdbc.statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.itsallcode.jdbc.ParameterMapper;

public class ConvertingPreparedStatement extends DelegatingPreparedStatement {

    private final ParameterMapper parameterMapper;

    public ConvertingPreparedStatement(final PreparedStatement delegate, final ParameterMapper parameterMapper) {
        super(delegate);
        this.parameterMapper = parameterMapper;
    }

    @Override
    public void setObject(final int parameterIndex, final Object x) throws SQLException {
        super.setObject(parameterIndex, convert(x));
    }

    private Object convert(final Object object) {
        return parameterMapper.map(object);
    }
}
