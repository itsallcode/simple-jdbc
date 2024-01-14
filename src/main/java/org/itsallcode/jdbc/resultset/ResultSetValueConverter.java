package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.itsallcode.jdbc.resultset.generic.ColumnMetaData;
import org.itsallcode.jdbc.resultset.generic.SimpleMetaData;

/**
 * Helper class used by {@link ConvertingResultSet}.
 */
class ResultSetValueConverter {
    private final Map<Integer, ColumnValueConverter> convertersByIndex;
    private final Map<String, Integer> columnIndexByLabel;

    private ResultSetValueConverter(final Map<Integer, ColumnValueConverter> convertersByIndex,
            final Map<String, Integer> columnIndexByLabel) {
        this.convertersByIndex = convertersByIndex;
        this.columnIndexByLabel = columnIndexByLabel;
    }

    static ResultSetValueConverter create(final SimpleMetaData resultSetMetadata,
            final List<ColumnValueConverter> converters) {
        final Map<Integer, ColumnValueConverter> convertersByIndex = new HashMap<>();
        final Map<String, Integer> columnIndexByLabel = new HashMap<>();
        for (int i = 1; i <= converters.size(); i++) {
            final ColumnValueConverter converter = converters.get(i - 1);
            final ColumnMetaData metaData = resultSetMetadata.getColumnByIndex(i);
            convertersByIndex.put(metaData.columnIndex(), converter);
            columnIndexByLabel.put(metaData.label(), i);
        }
        return new ResultSetValueConverter(convertersByIndex, columnIndexByLabel);
    }

    <T> T getObject(final ResultSet resultSet, final int columnIndex, final Class<T> type) throws SQLException {
        return getConverter(columnIndex).getObject(resultSet, columnIndex, type);
    }

    Object getObject(final ResultSet resultSet, final int columnIndex) throws SQLException {
        return getConverter(columnIndex).getObject(resultSet, columnIndex);
    }

    <T> T getObject(final ResultSet resultSet, final String columnLabel, final Class<T> type)
            throws SQLException {
        final int columnIndex = getIndexForLabel(columnLabel);
        return getConverter(columnIndex).getObject(resultSet, columnIndex, type);
    }

    Object getObject(final ResultSet resultSet, final String columnLabel)
            throws SQLException {
        final int columnIndex = getIndexForLabel(columnLabel);
        return getConverter(columnIndex).getObject(resultSet, columnIndex);
    }

    private int getIndexForLabel(final String columnLabel) {
        final Integer index = columnIndexByLabel.get(columnLabel.toUpperCase());
        if (index == null) {
            throw new IllegalStateException("No index found for column label '" + columnLabel
                    + "'. Available column labels: " + columnIndexByLabel.keySet());
        }
        return index.intValue();
    }

    private ColumnValueConverter getConverter(final int columnIndex) {
        final ColumnValueConverter converter = convertersByIndex.get(columnIndex);
        if (converter == null) {
            throw new IllegalStateException("No converter found for column index " + columnIndex);
        }
        return converter;
    }
}
