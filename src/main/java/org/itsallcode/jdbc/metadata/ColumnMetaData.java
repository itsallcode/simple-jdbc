package org.itsallcode.jdbc.metadata;

import java.sql.*;
import java.util.Arrays;

/**
 * Description of a column.
 * 
 * @param tableCatalog      table catalog (may be {@code null})
 * @param tableSchema       table schema (may be {@code null})
 * @param tableName         table name
 * @param columnName        column name
 * @param dataType          SQL type
 * @param typeName          Data source dependent type name, for a UDT the type
 *                          name is fully qualified
 * @param columnSize        column size.
 * @param decimalDigits     the number of fractional digits. {@code null} is
 *                          returned for data types where DECIMAL_DIGITS is not
 *                          applicable.
 * @param numPrecisionRadix Radix (typically either 10 or 2)
 * @param nullable          is {@code NULL} allowed.
 * @param remarks           comment describing column (may be {@code null})
 * @param columnDef         default value for the column, which should be
 *                          interpreted as a string when the value is enclosed
 *                          in single quotes (may be {@code null})
 * @param charOctetLength   for char types the maximum number of bytes in the
 *                          column
 * @param ordinalPosition   index of column in table (starting at 1)
 * @param isNullable        ISO rules are used to determine the nullability for
 *                          a column.
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
 * @param isGeneratedColumn Indicates whether this is a generated column
 * @see DatabaseMetaData#getColumns(String, String, String, String)
 */
public record ColumnMetaData(
        String tableCatalog,
        String tableSchema,
        String tableName,
        String columnName,
        JDBCType dataType,
        String typeName,
        int columnSize,
        int decimalDigits,
        int numPrecisionRadix,
        Nullability nullable,
        String remarks,
        String columnDef,
        int charOctetLength,
        int ordinalPosition,
        ISONullability isNullable,
        String scopeCatalog,
        String scopeSchema,
        String scopeTable,
        short sourceDataType,
        AutoIncrement isAutoIncrement,
        Generated isGeneratedColumn) {

    static ColumnMetaData create(final ResultSet rs) throws SQLException {
        return new ColumnMetaData(
                rs.getString("TABLE_CAT"),
                rs.getString("TABLE_SCHEM"),
                rs.getString("TABLE_NAME"),
                rs.getString("COLUMN_NAME"),
                JDBCType.valueOf(rs.getInt("DATA_TYPE")),
                rs.getString("TYPE_NAME"),
                rs.getInt("COLUMN_SIZE"),
                rs.getInt("DECIMAL_DIGITS"),
                rs.getInt("NUM_PREC_RADIX"),
                Nullability.valueOf(rs.getInt("NULLABLE")),
                rs.getString("REMARKS"),
                rs.getString("COLUMN_DEF"),
                rs.getInt("CHAR_OCTET_LENGTH"),
                rs.getInt("ORDINAL_POSITION"),
                ISONullability.valueOfNullability(rs.getString("IS_NULLABLE")),
                rs.getString("SCOPE_CATALOG"),
                rs.getString("SCOPE_SCHEMA"),
                rs.getString("SCOPE_TABLE"),
                rs.getShort("SOURCE_DATA_TYPE"),
                AutoIncrement.valueOfAutoIncrement(rs.getString("IS_AUTOINCREMENT")),
                Generated.valueOfGenerated(rs.getString("IS_GENERATEDCOLUMN")));
    }

    /**
     * Column nullability.
     */
    public enum Nullability {
        /** Column might not allow {@code NULL} values. */
        NO_NULLS(DatabaseMetaData.columnNoNulls),
        /** Column definitely allows {@code NULL} values. */
        NULLABLE(DatabaseMetaData.columnNullable),
        /** nullability unknown */
        NULLABLE_UNKNOWN(DatabaseMetaData.columnNullableUnknown);

        private final int value;

        Nullability(final int value) {
            this.value = value;
        }

        static Nullability valueOf(final int value) {
            return Arrays.stream(Nullability.values())
                    .filter(n -> n.value == value)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Unknown value %d for nullability".formatted(value)));
        }
    }

    /**
     * Column ISO nullability.
     */
    public enum ISONullability {
        /** Column can include NULLs. */
        NO_NULLS("YES"),
        /** Column cannot include NULLs. */
        NULLABLE("NO"),
        /** Nullability for the column is unknown */
        NULLABLE_UNKNOWN("");

        private final String value;

        ISONullability(final String value) {
            this.value = value;
        }

        static ISONullability valueOfNullability(final String value) {
            return Arrays.stream(ISONullability.values())
                    .filter(n -> n.value.equals(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Unknown value '%s' for ISO nullability".formatted(value)));
        }
    }

    /**
     * Indicates whether a column is auto incremented.
     */
    public enum AutoIncrement {
        /** Column is auto incremented. */
        AUTO_INCREMENT("YES"),
        /** Column is not auto incremented. */
        NO_AUTO_INCREMENT("NO"),
        /** It cannot be determined whether the column is auto incremented. */
        UNKNOWN("");

        private final String value;

        AutoIncrement(final String value) {
            this.value = value;
        }

        static AutoIncrement valueOfAutoIncrement(final String value) {
            return Arrays.stream(AutoIncrement.values())
                    .filter(n -> n.value.equals(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Unknown value '%s' for auto increment".formatted(value)));
        }
    }

    /**
     * Indicates whether this is a generated column.
     */
    public enum Generated {
        /** This a generated column. */
        GENERATED("YES"),
        /** This not a generated column. */
        NOT_GENERATED("NO"),
        /** It cannot be determined whether this is a generated column. */
        UNKNOWN("");

        private final String value;

        Generated(final String value) {
            this.value = value;
        }

        static Generated valueOfGenerated(final String value) {
            return Arrays.stream(Generated.values())
                    .filter(n -> n.value.equals(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Unknown value '%s' for column generated".formatted(value)));
        }
    }
}
