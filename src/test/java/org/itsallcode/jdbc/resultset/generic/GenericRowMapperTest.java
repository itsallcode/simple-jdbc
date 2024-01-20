package org.itsallcode.jdbc.resultset.generic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.sql.*;

import org.itsallcode.jdbc.UncheckedSQLException;
import org.itsallcode.jdbc.dialect.ColumnValueExtractor;
import org.itsallcode.jdbc.dialect.DbDialect;
import org.itsallcode.jdbc.resultset.generic.GenericRowMapper.ColumnValuesConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GenericRowMapperTest {

    private static final int ROW_NUM = 42;
    @Mock
    DbDialect dbDialectMock;
    @Mock
    ColumnValuesConverter<RowType> converterMock;
    @Mock
    ResultSet resultSetMock;
    @Mock
    ResultSetMetaData resultSetMetadataMock;
    @Mock
    ColumnValueExtractor columnValueExtractorMock;
    final RowType expectedRow = new RowType();

    @Test
    void mapRowNoColumn() throws SQLException {
        final RowType row = mapRow(0);
        assertThat(row).isSameAs(expectedRow);
    }

    @Test
    void mapRowSuccess() throws SQLException {
        final RowType row = mapRow(2);
        assertThat(row).isSameAs(expectedRow);
    }

    @Test
    void mapRowGetObjectFails() throws SQLException {
        when(columnValueExtractorMock.getObject(same(resultSetMock), eq(1))).thenThrow(new SQLException("expected"));
        assertThatThrownBy(() -> mapRow(2)).isInstanceOf(UncheckedSQLException.class).hasMessage(
                "Error extracting value for row 42 / column ColumnMetaData[columnIndex=1, name=null, label=null, type=ColumnType[jdbcType=NULL, typeName=null, className=null, precision=0, scale=0, displaySize=0]]: expected");
    }

    RowType mapRow(final int columnCount) throws SQLException {
        when(resultSetMock.getMetaData()).thenReturn(resultSetMetadataMock);
        when(resultSetMetadataMock.getColumnCount()).thenReturn(columnCount);
        lenient().when(dbDialectMock.createExtractor(any(ColumnMetaData.class))).thenReturn(columnValueExtractorMock);
        lenient().when(converterMock.mapRow(any(Row.class))).thenReturn(expectedRow);
        return new GenericRowMapper<>(dbDialectMock, converterMock).mapRow(resultSetMock, ROW_NUM);
    }

    record RowType() {
    }
}
