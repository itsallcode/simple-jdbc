package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

class ConvertingResultSet extends DelegatingResultSet {
    private final ResultSet delegate;

    ConvertingResultSet(final ResultSet delegate) {
        super(delegate);
        this.delegate = delegate;
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
