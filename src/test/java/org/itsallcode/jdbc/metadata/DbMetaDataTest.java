package org.itsallcode.jdbc.metadata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.itsallcode.jdbc.Context;
import org.itsallcode.jdbc.UncheckedSQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DbMetaDataTest {
    @Mock
    DatabaseMetaData metaDataMock;

    @Test
    void getTables() {
        assertThat(testee().getTables("catalog", "schema", "table", new String[] { "type" })).isNotNull();
    }

    @Test
    void getTablesFails() throws SQLException {
        when(metaDataMock.getTables("catalog", "schema", "table", new String[] { "type" }))
                .thenThrow(new SQLException("expected"));
        final DbMetaData testee = testee();
        assertThatThrownBy(() -> testee.getTables("catalog", "schema", "table", new String[] { "type" }))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error getting tables: expected");
    }

    @Test
    void getColumns() {
        assertThat(testee().getColumns("catalog", "schema", "table", "column")).isNotNull();
    }

    @Test
    void getColumnsFails() throws SQLException {
        when(metaDataMock.getColumns("catalog", "schema", "table", "column"))
                .thenThrow(new SQLException("expected"));
        final DbMetaData testee = testee();
        assertThatThrownBy(() -> testee.getColumns("catalog", "schema", "table", "column"))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error getting columns: expected");
    }

    DbMetaData testee() {
        return new DbMetaData(Context.builder().build(), metaDataMock);
    }
}
