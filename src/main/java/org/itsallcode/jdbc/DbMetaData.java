package org.itsallcode.jdbc;

import java.sql.*;

import org.itsallcode.jdbc.resultset.*;

/**
 * A simple wrapper for {@link DatabaseMetaData}.
 */
public class DbMetaData {

    private final DatabaseMetaData metaData;
    private final Context context;

    DbMetaData(final Context context, final DatabaseMetaData metaData) {
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

    /**
     * Description of a table.
     * 
     * @param tableCatalog              table catalog (may be {@code null})
     * @param tableSchema               table schema (may be {@code null})
     * @param tableName                 table name
     * @param tableType                 table type. Typical types are "TABLE",
     *                                  "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY",
     *                                  "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
     * @param remarks                   explanatory comment on the table (may be
     *                                  {@code null})
     * @param typeCatalog               the types catalog (may be {@code null})
     * @param typeSchema                the types schema (may be {@code null})
     * @param typeName                  type name (may be {@code null})
     * @param selfReferencingColumnName name of the designated "identifier" column
     *                                  of a typed table (may be {@code null})
     * @param refGeneration             specifies how values in
     *                                  SELF_REFERENCING_COL_NAME are created.
     *                                  Values are "SYSTEM", "USER", "DERIVED". (may
     *                                  be {@code null})
     * @see DatabaseMetaData#getTables(String, String, String, String[])
     */
    public record TableMetaData(String tableCatalog, String tableSchema, String tableName, String tableType,
            String remarks, String typeCatalog, String typeSchema, String typeName, String selfReferencingColumnName,
            String refGeneration) {
        private static TableMetaData create(final ResultSet rs, final int rowNum) throws SQLException {
            return new TableMetaData(
                    rs.getString("TABLE_CAT"), rs.getString("TABLE_SCHEM"),
                    rs.getString("TABLE_NAME"),
                    rs.getString("TABLE_TYPE"),
                    rs.getString("REMARKS"),
                    rs.getString("TYPE_CAT"), rs.getString("TYPE_SCHEM"),
                    rs.getString("TYPE_NAME"),
                    rs.getString("SELF_REFERENCING_COL_NAME"),
                    rs.getString("REF_GENERATION"));
        }
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

    /**
     * Description of a column.
     * 
     * @param tableCatalog      table catalog (may be {@code null})
     * @param tableSchema       table schema (may be {@code null})
     * @param tableName         table name
     * @param columnName        column name
     * @param dataType          SQL type from java.sql.Types
     * @param typeName          Data source dependent type name, for a UDT the type
     *                          name is fully qualified
     * @param columnSize        column size.
     * @param decimalDigits     the number of fractional digits. Null is returned
     *                          for data types where DECIMAL_DIGITS is not
     *                          applicable.
     * @param numPrecisionRadix Radix (typically either 10 or 2)
     * @param nullable          is NULL allowed.
     *                          <UL>
     *                          <LI>columnNoNulls - might not allow {@code NULL}
     *                          values
     *                          <LI>columnNullable - definitely allows {@code NULL}
     *                          values
     *                          <LI>columnNullableUnknown - nullability unknown
     *                          </UL>
     * @param remarks           comment describing column (may be {@code null})
     * @param columnDef         default value for the column, which should be
     *                          interpreted as a string when the value is enclosed
     *                          in single quotes (may be {@code null})
     * @param charOctetLength   for char types the maximum number of bytes in the
     *                          column
     * @param ordinalPosition   index of column in table (starting at 1)
     * @param isNullable        ISO rules are used to determine the nullability for
     *                          a column.
     *                          <UL>
     *                          <LI>YES --- if the column can include NULLs
     *                          <LI>NO --- if the column cannot include NULLs
     *                          <LI>empty string --- if the nullability for the
     *                          column is unknown
     *                          </UL>
     * @param scopeCatalog      catalog of table that is the scope of a reference
     *                          attribute ({@code null} if DATA_TYPE isn't REF)
     * @param scopeSchema       schema of table that is the scope of a reference
     *                          attribute ({@code null} if the DATA_TYPE isn't REF)
     * @param scopeTable        table name that this the scope of a reference
     *                          attribute ({@code null} if the DATA_TYPE isn't REF)
     * @param sourceDataType    source type of a distinct type or user-generated Ref
     *                          type, SQL type from java.sql.Types ({@code null} if
     *                          DATA_TYPE isn't DISTINCT or user-generated REF)
     * @param isAutoIncrement   Indicates whether this column is auto incremented
     *                          <UL>
     *                          <LI>YES --- if the column is auto incremented
     *                          <LI>NO --- if the column is not auto incremented
     *                          <LI>empty string --- if it cannot be determined
     *                          whether the column is auto incremented
     *                          </UL>
     * @param isGeneratedColumn Indicates whether this is a generated column
     *                          <UL>
     *                          <LI>YES --- if this a generated column
     *                          <LI>NO --- if this not a generated column
     *                          <LI>empty string --- if it cannot be determined
     *                          whether this is a generated column
     *                          </UL>
     * @see DatabaseMetaData#getColumns(String, String, String, String)
     */
    public record ColumnMetaData(
            String tableCatalog,
            String tableSchema,
            String tableName,
            String columnName,
            int dataType,
            String typeName,
            int columnSize,
            int decimalDigits,
            int numPrecisionRadix,
            String nullable,
            String remarks,
            String columnDef,
            int charOctetLength,
            int ordinalPosition,
            String isNullable,
            String scopeCatalog,
            String scopeSchema,
            String scopeTable,
            short sourceDataType,
            String isAutoIncrement,
            String isGeneratedColumn) {

        private static ColumnMetaData create(final ResultSet rs, final int rowNum) throws SQLException {
            return new ColumnMetaData(
                    rs.getString("TABLE_CAT"),
                    rs.getString("TABLE_SCHEM"),
                    rs.getString("TABLE_NAME"),
                    rs.getString("COLUMN_NAME"),
                    rs.getInt("DATA_TYPE"),
                    rs.getString("TYPE_NAME"),
                    rs.getInt("COLUMN_SIZE"),
                    rs.getInt("DECIMAL_DIGITS"),
                    rs.getInt("NUM_PREC_RADIX"),
                    rs.getString("NULLABLE"),
                    rs.getString("REMARKS"),
                    rs.getString("COLUMN_DEF"),
                    rs.getInt("CHAR_OCTET_LENGTH"),
                    rs.getInt("ORDINAL_POSITION"),
                    rs.getString("IS_NULLABLE"),
                    rs.getString("SCOPE_CATALOG"),
                    rs.getString("SCOPE_SCHEMA"),
                    rs.getString("SCOPE_TABLE"),
                    rs.getShort("SOURCE_DATA_TYPE"),
                    rs.getString("IS_AUTOINCREMENT"),
                    rs.getString("IS_GENERATEDCOLUMN"));
        }
    }
}
