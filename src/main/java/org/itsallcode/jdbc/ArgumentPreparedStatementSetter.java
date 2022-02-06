package org.itsallcode.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.itsallcode.jdbc.update.ParameterMapper;

public class ArgumentPreparedStatementSetter implements PreparedStatementSetter
{
    private Object[] args;
    private ParameterMapper mapper;

    public ArgumentPreparedStatementSetter(ParameterMapper mapper, Object[] args)
    {
        this.mapper = mapper;
        this.args = args;
    }

    public void setValues(PreparedStatement preparedStatement) throws SQLException
    {
        int parameterIndex = 1;
        for (final Object arg : args)
        {
            preparedStatement.setObject(parameterIndex, mapper.map(arg));
            parameterIndex++;
        }
    }
}
