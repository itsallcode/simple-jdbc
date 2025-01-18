package org.itsallcode.jdbc.metadata;

import java.sql.*;

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
    static TableMetaData create(final ResultSet rs, final int rowNum) throws SQLException {
        return new TableMetaData(
                rs.getString("TABLE_CAT"),
                rs.getString("TABLE_SCHEM"),
                rs.getString("TABLE_NAME"),
                rs.getString("TABLE_TYPE"),
                rs.getString("REMARKS"),
                rs.getString("TYPE_CAT"),
                rs.getString("TYPE_SCHEM"),
                rs.getString("TYPE_NAME"),
                rs.getString("SELF_REFERENCING_COL_NAME"),
                rs.getString("REF_GENERATION"));
    }
}
