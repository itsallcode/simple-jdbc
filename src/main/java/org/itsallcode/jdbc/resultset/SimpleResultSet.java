package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.*;

import org.itsallcode.jdbc.Context;
import org.itsallcode.jdbc.UncheckedSQLException;

/**
 * This class wraps a {@link ResultSet} and allows easy iteration via
 * {@link Iterator}, {@link List} or {@link Stream}.
 * 
 * @param <T> row type
 */
public class SimpleResultSet<T> implements AutoCloseable, Iterable<T> {
    private final ResultSet resultSet;
    private final RowMapper<T> rowMapper;
    private final Context context;
    private Iterator<T> iterator;

    /**
     * Create a new instance.
     * 
     * @param context   database context
     * @param resultSet the underlying result set
     * @param rowMapper a row mapper for converting each row
     */
    public SimpleResultSet(final Context context, final ResultSet resultSet, final RowMapper<T> rowMapper) {
        this.context = context;
        this.resultSet = resultSet;
        this.rowMapper = rowMapper;
    }

    /**
     * Get in {@link Iterator} of all rows.
     * 
     * @return an interator with all rows.
     */
    @Override
    public Iterator<T> iterator() {
        if (iterator != null) {
            throw new IllegalStateException("Only one iterator allowed per ResultSet");
        }
        iterator = ResultSetIterator.create(context, this, rowMapper);
        return iterator;
    }

    /**
     * Collect all rows to a list.
     * 
     * @return a list with all rows.
     */
    public List<T> toList() {
        return stream().collect(Collectors.toList());
    }

    /**
     * Get a stream of all rows.
     * 
     * @return a stream with all rows.
     */
    public Stream<T> stream() {
        final Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(this.iterator(), Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false)
                .onClose(this::close);
    }

    /**
     * Close the underlying {@link ResultSet}.
     * 
     * @throws UncheckedSQLException if closing fails.
     */
    @Override
    public void close() {
        try {
            resultSet.close();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error closing resultset", e);
        }
    }

    private boolean next() {
        try {
            return resultSet.next();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error getting next row", e);
        }
    }

    private static class ResultSetIterator<T> implements Iterator<T> {
        private final Context context;
        private final SimpleResultSet<T> resultSet;
        private final RowMapper<T> rowMapper;
        private boolean hasNext;
        private int currentRowIndex = 0;

        private ResultSetIterator(final Context context, final SimpleResultSet<T> simpleResultSet,
                final RowMapper<T> rowMapper,
                final boolean hasNext) {
            this.context = context;
            this.resultSet = simpleResultSet;
            this.rowMapper = rowMapper;
            this.hasNext = hasNext;
        }

        public static <T> Iterator<T> create(final Context context, final SimpleResultSet<T> simpleResultSet,
                final RowMapper<T> rowMapper) {
            final boolean firstRowExists = simpleResultSet.next();
            return new ResultSetIterator<>(context, simpleResultSet, rowMapper, firstRowExists);
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public T next() {
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            final T row = mapRow();
            hasNext = resultSet.next();
            currentRowIndex++;
            return row;
        }

        private T mapRow() {
            try {
                return rowMapper.mapRow(context, resultSet.resultSet, currentRowIndex);
            } catch (final SQLException e) {
                throw new UncheckedSQLException("Error mapping row " + currentRowIndex, e);
            }
        }
    }
}
