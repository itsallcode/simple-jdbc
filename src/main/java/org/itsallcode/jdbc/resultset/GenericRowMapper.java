package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.itsallcode.jdbc.Context;

/**
 * This {@link RowMapper} converts a row to the generic {@link Row} type.
 */
public class GenericRowMapper implements RowMapper<Row> {
    private final Context context;
    private ResultSetRowBuilder rowBuilder;

    /**
     * Create a new instance.
     * 
     * @param context context
     */
    public GenericRowMapper(final Context context) {
        this.context = context;
    }

    @Override
    public Row mapRow(final ResultSet resultSet, final int rowNum) throws SQLException {
        if (rowBuilder == null) {
            rowBuilder = new ResultSetRowBuilder(SimpleMetaData.create(resultSet.getMetaData(), context));
        }
        return rowBuilder.buildRow(resultSet, rowNum);
    }
}
