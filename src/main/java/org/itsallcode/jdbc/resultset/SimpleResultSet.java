package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.itsallcode.jdbc.Context;
import org.itsallcode.jdbc.UncheckedSQLException;

public class SimpleResultSet implements AutoCloseable, Iterable<ResultSetRow>
{
    private final ResultSet resultSet;
    private final Context context;
    private Iterator<ResultSetRow> iterator;

    public SimpleResultSet(ResultSet resultSet, Context context)
    {
        this.resultSet = resultSet;
        this.context = context;
    }

    @Override
    public Iterator<ResultSetRow> iterator()
    {
        if (iterator != null)
        {
            throw new IllegalStateException("Only one iterator allowed per ResultSet");
        }
        iterator = ResultSetIterator.create(this);
        return iterator;
    }

    public Stream<ResultSetRow> stream()
    {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(this.iterator(), 0), false);
    }

    @Override
    public void close()
    {
        try
        {
            resultSet.close();
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error closing resultset", e);
        }
    }

    private boolean next()
    {
        try
        {
            return resultSet.next();
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error getting next row", e);
        }
    }

    private SimpleMetaData getMetaData()
    {
        try
        {
            return SimpleMetaData.create(resultSet.getMetaData(), context);
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error getting metadata", e);
        }
    }

    private static class ResultSetIterator implements Iterator<ResultSetRow>
    {
        private boolean hasNext;
        private int currentRowIndex = 0;
        private final SimpleResultSet resultSet;
        private final ResultSetRowBuilder resultSetRowBuilder;

        private ResultSetIterator(SimpleResultSet simpleResultSet, boolean hasNext)
        {
            this.resultSet = simpleResultSet;
            this.hasNext = hasNext;
            this.resultSetRowBuilder = new ResultSetRowBuilder(resultSet.resultSet, simpleResultSet.getMetaData());
        }

        public static Iterator<ResultSetRow> create(SimpleResultSet simpleResultSet)
        {
            final boolean firstRowExists = simpleResultSet.next();
            return new ResultSetIterator(simpleResultSet, firstRowExists);
        }

        @Override
        public boolean hasNext()
        {
            return hasNext;
        }

        @Override
        public ResultSetRow next()
        {
            if (!hasNext)
            {
                throw new NoSuchElementException();
            }
            final ResultSetRow row = resultSetRowBuilder.buildRow(currentRowIndex);
            hasNext = resultSet.next();
            currentRowIndex++;
            return row;
        }
    }
}
