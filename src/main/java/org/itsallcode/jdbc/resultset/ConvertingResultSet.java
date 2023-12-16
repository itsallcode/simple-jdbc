package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConvertingResultSet extends DelegatingResultSet {
    private final ResultSet delegate;
    private final ResultSetValueConverter converter;

    public ConvertingResultSet(final ResultSet delegate, final ResultSetValueConverter converter) {
        super(delegate);
        this.delegate = delegate;
        this.converter = converter;
    }

    @Override
    public <T> T getObject(final int columnIndex, final Class<T> type) throws SQLException {
        return converter.getObject(delegate, columnIndex, type);
    }

    @Override
    public <T> T getObject(final String columnLabel, final Class<T> type) throws SQLException {
        return converter.getObject(delegate, columnLabel, type);
    }

    @Override
    public Object getObject(final int columnIndex) throws SQLException {
        return converter.getObject(delegate, columnIndex);
    }

    @Override
    public Object getObject(final String columnLabel) throws SQLException {
        return converter.getObject(delegate, columnLabel);
    }
}
