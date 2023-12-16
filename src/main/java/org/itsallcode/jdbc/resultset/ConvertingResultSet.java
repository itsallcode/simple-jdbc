package org.itsallcode.jdbc.resultset;

import static java.util.stream.Collectors.toList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.itsallcode.jdbc.dialect.DbDialect;
import org.itsallcode.jdbc.resultset.generic.SimpleMetaData;

/**
 * A {@link ResultSet} that automatically converts values to modern types in the
 * following methods:
 * <ul>
 * <li>{@link ResultSet#getObject(String)}</li>
 * <li>{@link ResultSet#getObject(String, Class)}</li>
 * <li>{@link ResultSet#getObject(int)}</li>
 * <li>{@link ResultSet#getObject(int, Class)}</li>
 * </ul>
 */
public class ConvertingResultSet extends DelegatingResultSet {
    private final ResultSet delegate;
    private final ResultSetValueConverter converter;

    private ConvertingResultSet(final ResultSet delegate, final ResultSetValueConverter converter) {
        super(delegate);
        this.delegate = delegate;
        this.converter = converter;
    }

    /**
     * Create a new converting result set.
     * 
     * @param dialect  DB dialect
     * @param delegate the original result set.
     * @return a new converting result set.
     */
    public static ConvertingResultSet create(final DbDialect dialect, final ResultSet delegate) {
        final SimpleMetaData metaData = SimpleMetaData.create(delegate);
        final List<ColumnValueConverter> converters = metaData.getColumns().stream()
                .map(col -> ColumnValueConverter.simple(dialect.createExtractor(col)))
                .collect(toList());
        return new ConvertingResultSet(delegate, ResultSetValueConverter.create(metaData, converters));
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
