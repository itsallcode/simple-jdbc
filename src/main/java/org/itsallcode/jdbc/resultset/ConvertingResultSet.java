package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

class ConvertingResultSet extends DelegatingResultSet {
    private final ResultSet delegate;
    private final ValueConverter converter;

    ConvertingResultSet(final ResultSet delegate, final ValueConverter converter) {
        super(delegate);
        this.delegate = delegate;
        this.converter = converter;
    }

    @Override
    public <T> T getObject(final int columnIndex, final Class<T> type) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T getObject(final String columnLabel, final Class<T> type) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
