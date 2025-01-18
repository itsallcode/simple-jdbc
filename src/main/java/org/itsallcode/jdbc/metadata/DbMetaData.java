package org.itsallcode.jdbc.metadata;

import java.sql.*;

import org.itsallcode.jdbc.Context;
import org.itsallcode.jdbc.UncheckedSQLException;
import org.itsallcode.jdbc.resultset.*;

/**
 * A simple wrapper for {@link DatabaseMetaData}.
 */
public class DbMetaData {

    private final DatabaseMetaData metaData;
    private final Context context;

    /**
     * Create a new instance.
     * 
     * @param context  DB context.
     * @param metaData metaData object.
     */
    public DbMetaData(final Context context, final DatabaseMetaData metaData) {
        this.context = context;
        this.metaData = metaData;
    }

    /**
     * Retrieves a description of the tables available in the given catalog. Only
     * table descriptions matching the catalog, schema, table name and type criteria
     * are returned. They are ordered by {@code TABLE_TYPE}, {@code TABLE_CAT},
     * {@code TABLE_SCHEM} and {@code TABLE_NAME}.
     * 
     * @param catalog          a catalog name; must match the catalog name as it is
     *                         stored in the database; "" retrieves those without a
     *                         catalog; {@code null} means that the catalog name
     *                         should not be used to narrow the search
     * @param schemaPattern    a schema name pattern; must match the schema name as
     *                         it is stored in the database; "" retrieves those
     *                         without a schema; {@code null} means that the schema
     *                         name should not be used to narrow the search
     * @param tableNamePattern a table name pattern; must match the table name as it
     *                         is stored in the database
     * @param types            a list of table types, which must be from the list of
     *                         table types returned from
     *                         {@link DatabaseMetaData#getTableTypes()},to include;
     *                         {@code null} returns all types
     * @return table descriptions
     * @see DatabaseMetaData#getTables(String, String, String, String[])
     */
    public SimpleResultSet<TableMetaData> getTables(final String catalog, final String schemaPattern,
            final String tableNamePattern, final String[] types) {
        return createResultSet(getTablesInternal(catalog, schemaPattern, tableNamePattern, types),
                TableMetaData::create);
    }

    private ResultSet getTablesInternal(final String catalog, final String schemaPattern, final String tableNamePattern,
            final String[] types) {
        try {
            return metaData.getTables(catalog, schemaPattern, tableNamePattern, types);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error getting tables", e);
        }
    }

    /**
     * Retrieves a description of table columns available in the specified catalog.
     * <P>
     * Only column descriptions matching the catalog, schema, table and column name
     * criteria are returned. They are ordered by
     * {@code TABLE_CAT},{@code TABLE_SCHEM}, {@code TABLE_NAME}, and
     * {@code ORDINAL_POSITION}.
     * 
     * @param catalog           a catalog name; must match the catalog name as it is
     *                          stored in the database; "" retrieves those without a
     *                          catalog; {@code null} means that the catalog name
     *                          should not be used to narrow the search
     * @param schemaPattern     a schema name pattern; must match the schema name as
     *                          it is stored in the database; "" retrieves those
     *                          without a schema; {@code null} means that the schema
     *                          name should not be used to narrow the search
     * @param tableNamePattern  a table name pattern; must match the table name as
     *                          it is stored in the database
     * @param columnNamePattern a column name pattern; must match the column name as
     *                          it is stored in the database
     * @return column descriptions
     * @see DatabaseMetaData#getColumns(String, String, String, String)
     */
    public SimpleResultSet<ColumnMetaData> getColumns(final String catalog, final String schemaPattern,
            final String tableNamePattern, final String columnNamePattern) {
        return createResultSet(getColumnsInternal(catalog, schemaPattern, tableNamePattern, columnNamePattern),
                ColumnMetaData::create);
    }

    private ResultSet getColumnsInternal(final String catalog, final String schemaPattern,
            final String tableNamePattern, final String columnNamePattern) {
        try {
            return metaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error getting tables", e);
        }
    }

    private <T> SimpleResultSet<T> createResultSet(final ResultSet resultSet, final RowMapper<T> rowMapper) {
        return new SimpleResultSet<>(context, resultSet, ContextRowMapper.create(rowMapper), () -> {
        });
    }
}
