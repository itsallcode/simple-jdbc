package org.itsallcode.jdbc.metadata;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.JDBCType;
import java.util.List;

import org.itsallcode.jdbc.H2TestFixture;
import org.itsallcode.jdbc.SimpleConnection;
import org.itsallcode.jdbc.metadata.ColumnMetaData.*;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.junit.jupiter.api.Test;

class DbMetaDataITest {

    @Test
    void getTablesNoResult() {
        try (final SimpleConnection connection = H2TestFixture.createMemConnection()) {
            final List<TableMetaData> tables = connection.getMetaData().getTables("unknown", null, null, null).toList();
            assertThat(tables).isEmpty();
        }
    }

    @Test
    void getTables() {
        try (final SimpleConnection connection = H2TestFixture.createMemConnection()) {
            final List<TableMetaData> tables = connection.getMetaData().getTables(null, null, null, null).toList();
            assertThat(tables)
                    .hasSize(35)
                    .first()
                    .isEqualTo(new TableMetaData("UNNAMED", "INFORMATION_SCHEMA", "CONSTANTS", "BASE TABLE",
                            null, null, null, null, null, null));
        }
    }

    @Test
    void getTablesMetaData() {
        try (final SimpleConnection connection = H2TestFixture.createMemConnection();
                final SimpleResultSet<TableMetaData> result = connection.getMetaData().getTables(null, null, null,
                        null)) {
            assertThat(result.getMetaData().columns())
                    .extracting(org.itsallcode.jdbc.resultset.generic.ColumnMetaData::name).containsExactly("TABLE_CAT",
                            "TABLE_SCHEM", "TABLE_NAME", "TABLE_TYPE", "REMARKS", "TYPE_CAT", "TYPE_SCHEM", "TYPE_NAME",
                            "SELF_REFERENCING_COL_NAME", "REF_GENERATION");
        }
    }

    @Test
    void getColumnsNoResult() {
        try (final SimpleConnection connection = H2TestFixture.createMemConnection()) {
            final List<ColumnMetaData> columns = connection.getMetaData().getColumns("unknown", null, null, null)
                    .toList();
            assertThat(columns).isEmpty();
        }
    }

    @Test
    void getColumns() {
        try (final SimpleConnection connection = H2TestFixture.createMemConnection()) {
            final List<ColumnMetaData> columns = connection.getMetaData().getColumns(null, null, null, null).toList();
            assertThat(columns)
                    .hasSize(451)
                    .first()
                    .isEqualTo(new ColumnMetaData("UNNAMED", "INFORMATION_SCHEMA", "CHECK_CONSTRAINTS",
                            "CONSTRAINT_CATALOG", JDBCType.VARCHAR, "CHARACTER VARYING", 1000000000, 0, 0,
                            Nullability.NULLABLE, null,
                            null, 1000000000, 1, ISONullability.NO_NULLS, null, null, null, (short) 0,
                            AutoIncrement.NO_AUTO_INCREMENT, Generated.NOT_GENERATED));
        }
    }

    @Test
    void getColumnsFilterByTable() {
        try (final SimpleConnection connection = H2TestFixture.createMemConnection()) {
            final List<ColumnMetaData> columns = connection.getMetaData()
                    .getColumns(null, null, "CHECK_CONSTRAINTS", null).toList();
            assertThat(columns).hasSize(4);
        }
    }

    @Test
    void getColumnsFilterByColumn() {
        try (final SimpleConnection connection = H2TestFixture.createMemConnection()) {
            final List<ColumnMetaData> columns = connection.getMetaData()
                    .getColumns(null, null, "CHECK_CONSTRAINTS", "CONSTRAINT_CATALOG").toList();
            assertThat(columns).hasSize(1);
        }
    }

    @Test
    void getColumnsMetaData() {
        try (final SimpleConnection connection = H2TestFixture.createMemConnection();
                final SimpleResultSet<ColumnMetaData> result = connection.getMetaData().getColumns(null, null, null,
                        null)) {
            assertThat(result.getMetaData().columns())
                    .extracting(org.itsallcode.jdbc.resultset.generic.ColumnMetaData::name)
                    .containsExactly("TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE",
                            "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX",
                            "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
                            "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATALOG", "SCOPE_SCHEMA",
                            "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT", "IS_GENERATEDCOLUMN");
        }
    }
}
