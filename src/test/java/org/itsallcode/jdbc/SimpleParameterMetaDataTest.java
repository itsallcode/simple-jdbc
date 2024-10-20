package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.sql.*;

import org.itsallcode.jdbc.SimpleParameterMetaData.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

@ExtendWith(MockitoExtension.class)
class SimpleParameterMetaDataTest {
    @Mock
    ParameterMetaData parameterMetadataMock;

    @Test
    void testToString() {
        ToStringVerifier.forClass(SimpleParameterMetaData.class).verify();
    }

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(SimpleParameterMetaData.class).verify();
    }

    @Test
    void createNoParameters() {
        final SimpleParameterMetaData metaData = SimpleParameterMetaData.create(parameterMetadataMock);
        assertThat(metaData.parameters()).isEmpty();
    }

    @Test
    void singleParameter() throws SQLException {
        when(parameterMetadataMock.getParameterCount()).thenReturn(1);
        when(parameterMetadataMock.getParameterClassName(1)).thenReturn("className");
        when(parameterMetadataMock.getParameterType(1)).thenReturn(Types.VARCHAR);
        when(parameterMetadataMock.getParameterTypeName(1)).thenReturn("typeName");
        when(parameterMetadataMock.getParameterMode(1)).thenReturn(ParameterMetaData.parameterModeInOut);
        when(parameterMetadataMock.isNullable(1)).thenReturn(ParameterMetaData.parameterNoNulls);
        when(parameterMetadataMock.isSigned(1)).thenReturn(true);
        when(parameterMetadataMock.getScale(1)).thenReturn(42);
        when(parameterMetadataMock.getPrecision(1)).thenReturn(23);
        final SimpleParameterMetaData metaData = SimpleParameterMetaData.create(parameterMetadataMock);
        final Parameter param = metaData.parameters().get(0);
        assertAll(
                () -> assertThat(metaData.parameters()).hasSize(1),
                () -> assertThat(param.className()).isEqualTo("className"),
                () -> assertThat(param.type()).isEqualTo(JDBCType.VARCHAR),
                () -> assertThat(param.typeName()).isEqualTo("typeName"),
                () -> assertThat(param.mode()).isEqualTo(ParameterMode.INOUT),
                () -> assertThat(param.nullable()).isEqualTo(ParameterNullable.NO_NULLS),
                () -> assertThat(param.precision()).isEqualTo(23),
                () -> assertThat(param.scale()).isEqualTo(42),
                () -> assertThat(param.signed()).isTrue());
    }

    @Test
    void getParameterFails() throws SQLException {
        when(parameterMetadataMock.getParameterCount()).thenThrow(new SQLException("mock"));
        assertThatThrownBy(() -> SimpleParameterMetaData.create(parameterMetadataMock))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error getting parameter metadata: mock");
    }

    @Test
    void invalidNullable() throws SQLException {
        when(parameterMetadataMock.getParameterCount()).thenReturn(1);
        when(parameterMetadataMock.isNullable(1)).thenReturn(-99);
        assertThatThrownBy(() -> SimpleParameterMetaData.create(parameterMetadataMock))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No parameter nullable mode found for value -99");
    }

    @Test
    void invalidMode() throws SQLException {
        when(parameterMetadataMock.getParameterCount()).thenReturn(1);
        when(parameterMetadataMock.getParameterMode(1)).thenReturn(-99);
        assertThatThrownBy(() -> SimpleParameterMetaData.create(parameterMetadataMock))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No parameter mode found for value -99");
    }
}
