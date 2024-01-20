package org.itsallcode.jdbc.resultset.generic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.sql.*;
import java.util.List;

import org.itsallcode.jdbc.UncheckedSQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

@ExtendWith(MockitoExtension.class)
class ColumnMetaDataTest {
    @Mock
    ResultSet resultSetMock;
    @Mock
    ResultSetMetaData resultSetMetadataMock;

    @Test
    void testToString() {
        ToStringVerifier.forClass(ColumnMetaData.class).verify();
    }

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ColumnMetaData.class).verify();
    }

    @Test
    void createFails() throws SQLException {
        when(resultSetMock.getMetaData()).thenThrow(new SQLException("expected"));
        assertThatThrownBy(() -> ColumnMetaData.create(resultSetMock)).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error extracting meta data: expected");
    }

    @Test
    void noColumns() throws SQLException {
        when(resultSetMock.getMetaData()).thenReturn(resultSetMetadataMock);
        when(resultSetMetadataMock.getColumnCount()).thenReturn(0);
        final List<ColumnMetaData> columns = ColumnMetaData.create(resultSetMock);
        assertThat(columns).isEmpty();
    }

    @Test
    void singleColumn() throws SQLException {
        when(resultSetMock.getMetaData()).thenReturn(resultSetMetadataMock);
        when(resultSetMetadataMock.getColumnCount()).thenReturn(1);
        when(resultSetMetadataMock.getColumnLabel(1)).thenReturn("label");
        when(resultSetMetadataMock.getColumnName(1)).thenReturn("name");
        when(resultSetMetadataMock.getPrecision(1)).thenReturn(23);
        when(resultSetMetadataMock.getColumnClassName(1)).thenReturn("className");
        when(resultSetMetadataMock.getColumnDisplaySize(1)).thenReturn(17);
        when(resultSetMetadataMock.getScale(1)).thenReturn(13);
        when(resultSetMetadataMock.getColumnType(1)).thenReturn(Types.VARCHAR);
        when(resultSetMetadataMock.getColumnTypeName(1)).thenReturn("typeName");

        final List<ColumnMetaData> columns = ColumnMetaData.create(resultSetMock);
        assertThat(columns).containsExactly(
                new ColumnMetaData(1, "name", "label",
                        new ColumnType(JDBCType.VARCHAR, "typeName", "className", 23, 13, 17)));
    }
}
