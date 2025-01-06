package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    private final ContextRowMapper<T> rowMapper;
    private final Context context;
    private final AutoCloseable statement;
    private Iterator<T> iterator;

    /**
     * Create a new instance.
     * 
     * @param context   database context
     * @param resultSet the underlying result set
     * @param rowMapper a row mapper for converting each row
     * @param statement the statement that created the result set. This will be
     *                  closed when the result set is closed.
     */
    public SimpleResultSet(final Context context, final ResultSet resultSet, final ContextRowMapper<T> rowMapper,
            final AutoCloseable statement) {
        this.context = context;
        this.resultSet = resultSet;
        this.rowMapper = rowMapper;
        this.statement = statement;
    }

    /**
     * Get in {@link Iterator} of all rows.
     * 
     * @return an iterator with all rows.
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
        try (Stream<T> stream = stream()) {
            return stream.toList();
        }
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
     * Close the underlying {@link ResultSet} and the statement that created it.
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
        try {
            statement.close();
        } catch (final Exception e) {
            throw new IllegalStateException("Error closing statement: " + e.getMessage(), e);
        }
    }

    private boolean next() {
        try {
            return resultSet.next();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error getting next row", e);
        }
    }

    private static final class ResultSetIterator<T> implements Iterator<T> {
        private final Context context;
        private final SimpleResultSet<T> resultSet;
        private final ContextRowMapper<T> rowMapper;
        private boolean hasNext;
        private int currentRowIndex;

        private ResultSetIterator(final Context context, final SimpleResultSet<T> simpleResultSet,
                final ContextRowMapper<T> rowMapper,
                final boolean hasNext) {
            this.context = context;
            this.resultSet = simpleResultSet;
            this.rowMapper = rowMapper;
            this.hasNext = hasNext;
        }

        private static <T> Iterator<T> create(final Context context, final SimpleResultSet<T> simpleResultSet,
                final ContextRowMapper<T> rowMapper) {
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
            } catch (final RuntimeException e) {
                throw new IllegalStateException("Error mapping row " + currentRowIndex + ": " + e.getMessage(), e);
            }
        }
    }
}
