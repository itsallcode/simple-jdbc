package org.itsallcode.jdbc.resultset.generic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.sql.*;

import org.itsallcode.jdbc.UncheckedSQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

@ExtendWith(MockitoExtension.class)
class SimpleMetaDataTest {
    @Mock
    ResultSet resultSetMock;
    @Mock
    ResultSetMetaData resultSetMetadataMock;

    @Test
    void testToString() {
        ToStringVerifier.forClass(SimpleMetaData.class).verify();
    }

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(SimpleMetaData.class).verify();
    }

    @Test
    void createNoColumn() throws SQLException {
        when(resultSetMock.getMetaData()).thenReturn(resultSetMetadataMock);
        when(resultSetMetadataMock.getColumnCount()).thenReturn(0);
        final SimpleMetaData simpleMetaData = SimpleMetaData.create(resultSetMock);
        assertThat(simpleMetaData.columns()).isEmpty();
    }

    @Test
    void createFails() throws SQLException {
        when(resultSetMock.getMetaData()).thenThrow(new SQLException("expected"));
        assertThatThrownBy(() -> SimpleMetaData.create(resultSetMock)).isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error extracting meta data: expected");
    }
}
