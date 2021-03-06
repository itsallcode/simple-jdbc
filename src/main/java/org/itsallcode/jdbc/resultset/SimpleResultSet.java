package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.itsallcode.jdbc.UncheckedSQLException;

public class SimpleResultSet<T> implements AutoCloseable, Iterable<T> {
    private final ResultSet resultSet;
    private Iterator<T> iterator;
    private final RowMapper<T> rowMapper;

    public SimpleResultSet(ResultSet resultSet, RowMapper<T> rowMapper) {
        this.resultSet = resultSet;
        this.rowMapper = rowMapper;
    }

    @Override
    public Iterator<T> iterator() {
        if (iterator != null) {
            throw new IllegalStateException("Only one iterator allowed per ResultSet");
        }
        iterator = ResultSetIterator.create(this, rowMapper);
        return iterator;
    }

    public List<T> toList() {
        return stream().collect(Collectors.toList());
    }

    public Stream<T> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this.iterator(), Spliterator.ORDERED), false)
                .onClose(this::close);
    }

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
        private boolean hasNext;
        private int currentRowIndex = 0;
        private final SimpleResultSet<T> resultSet;
        private final RowMapper<T> rowMapper;

        private ResultSetIterator(SimpleResultSet<T> simpleResultSet, RowMapper<T> rowMapper, boolean hasNext) {
            this.resultSet = simpleResultSet;
            this.rowMapper = rowMapper;
            this.hasNext = hasNext;
        }

        public static <T> Iterator<T> create(SimpleResultSet<T> simpleResultSet, RowMapper<T> rowMapper) {
            final boolean firstRowExists = simpleResultSet.next();
            return new ResultSetIterator<>(simpleResultSet, rowMapper, firstRowExists);
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
                return rowMapper.mapRow(resultSet.resultSet, currentRowIndex);
            } catch (final SQLException e) {
                throw new UncheckedSQLException("Error mapping row " + currentRowIndex, e);
            }
        }
    }
}
