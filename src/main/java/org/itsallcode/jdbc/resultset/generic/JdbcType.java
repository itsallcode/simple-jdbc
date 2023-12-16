package org.itsallcode.jdbc.resultset.generic;

import java.sql.Types;
import java.util.Arrays;

/**
 * Enum for values in {@link Types}.
 */
public enum JdbcType {
    /** SQL type {@code BIT} */
    BIT(Types.BIT),
    /** SQL type {@code TINYINT} */
    TINYINT(Types.TINYINT),
    /** SQL type {@code SMALLINT} */
    SMALLINT(Types.SMALLINT),
    /** SQL type {@code INTEGER} */
    INTEGER(Types.INTEGER),
    /** SQL type {@code BIGINT} */
    BIGINT(Types.BIGINT),
    /** SQL type {@code FLOAT} */
    FLOAT(Types.FLOAT),
    /** SQL type {@code REAL} */
    REAL(Types.REAL),
    /** SQL type {@code DOUBLE} */
    DOUBLE(Types.DOUBLE),
    /** SQL type {@code NUMERIC} */
    NUMERIC(Types.NUMERIC),
    /** SQL type {@code DECIMAL} */
    DECIMAL(Types.DECIMAL),
    /** SQL type {@code CHAR} */
    CHAR(Types.CHAR),
    /** SQL type {@code VARCHAR} */
    VARCHAR(Types.VARCHAR),
    /** SQL type {@code LONGVARCHAR} */
    LONGVARCHAR(Types.LONGVARCHAR),
    /** SQL type {@code DATE} */
    DATE(Types.DATE),
    /** SQL type {@code TIME} */
    TIME(Types.TIME),
    /** SQL type {@code TIMESTAMP} */
    TIMESTAMP(Types.TIMESTAMP),
    /** SQL type {@code BINARY} */
    BINARY(Types.BINARY),
    /** SQL type {@code VARBINARY} */
    VARBINARY(Types.VARBINARY),
    /** SQL type {@code LONGVARBINARY} */
    LONGVARBINARY(Types.LONGVARBINARY),
    /** SQL type {@code NULL} */
    NULL(Types.NULL),
    /** Database specific type */
    OTHER(Types.OTHER),
    /** SQL type {@code JAVA_OBJECT} */
    JAVA_OBJECT(Types.JAVA_OBJECT),
    /** SQL type {@code DISTINCT} */
    DISTINCT(Types.DISTINCT),
    /** SQL type {@code STRUCT} */
    STRUCT(Types.STRUCT),
    /** SQL type {@code ARRAY} */
    ARRAY(Types.ARRAY),
    /** SQL type {@code BLOB} */
    BLOB(Types.BLOB),
    /** SQL type {@code CLOB} */
    CLOB(Types.CLOB),
    /** SQL type {@code REF} */
    REF(Types.REF),
    /** SQL type {@code DATALINK} */
    DATALINK(Types.DATALINK),
    /** SQL type {@code BOOLEAN} */
    BOOLEAN(Types.BOOLEAN),
    /** SQL type {@code ROWID} */
    ROWID(Types.ROWID),
    /** SQL type {@code NCHAR} */
    NCHAR(Types.NCHAR),
    /** SQL type {@code NVARCHAR} */
    NVARCHAR(Types.NVARCHAR),
    /** SQL type {@code LONGNVARCHAR} */
    LONGNVARCHAR(Types.LONGNVARCHAR),
    /** SQL type {@code NCLOB} */
    NCLOB(Types.NCLOB),
    /** SQL type {@code XML} */
    SQLXML(Types.SQLXML),
    /** SQL type {@code REF CURSOR} */
    REF_CURSOR(Types.REF_CURSOR),
    /** SQL type {@code TIME WITH TIMEZONE} */
    TIME_WITH_TIMEZONE(Types.TIME_WITH_TIMEZONE),
    /** SQL type {@code TIMESTAMP WITH TIMEZONE} */
    TIMESTAMP_WITH_TIMEZONE(Types.TIMESTAMP_WITH_TIMEZONE);

    private final int type;

    private JdbcType(final int type) {
        this.type = type;
    }

    /**
     * Get the {@link JdbcType} for the given value from {@link Types}.
     * 
     * @param type the type to convert
     * @return {@link JdbcType} value
     */
    public static JdbcType forType(final int type) {
        return Arrays.stream(values())
                .filter(t -> t.type == type).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No JDBC type found for value " + type));
    }
}
