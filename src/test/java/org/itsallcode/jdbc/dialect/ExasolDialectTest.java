package org.itsallcode.jdbc.dialect;

import static org.mockito.Mockito.verify;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExasolDialectTest {
    @Mock
    PreparedStatement stmtMock;

    static Stream<Arguments> mappedTypes() {
        return Stream.of(mapType("test", "test"), mapType(1, 1), mapType(1L, 1L),
                mapType(1.0, 1.0), mapType(1.0f, 1.0f), mapType(true, true), mapType(false, false));
    }

    static Arguments mapType(final Object input, final Object expected) {
        return Arguments.of(input, expected);
    }

    @ParameterizedTest
    @MethodSource("mappedTypes")
    void createGenericSetter(final Object input, final Object expected) throws SQLException {
        @SuppressWarnings("unchecked")
        final ColumnValueSetter<Object> setter = (ColumnValueSetter<Object>) new ExasolDialect()
                .createSetter(input.getClass());

        setter.setObject(stmtMock, 0, input);
        verify(stmtMock).setObject(0, expected);
    }

    static Stream<Arguments> mappedTypesToString() {
        return Stream.of(mapType(LocalDate.of(2024, 9, 1), "2024-09-01"),
                mapType(Instant.parse("2007-12-03T10:15:30.00Z"), "2007-12-03 10:15:30.000"),
                mapType(LocalDateTime.parse("2007-12-03T10:15:30"), "2007-12-03 10:15:30.000"));
    }

    @ParameterizedTest
    @MethodSource("mappedTypesToString")
    void createToStringSetter(final Object input, final Object expected) throws SQLException {
        @SuppressWarnings("unchecked")
        final ColumnValueSetter<Object> setter = (ColumnValueSetter<Object>) new ExasolDialect()
                .createSetter(input.getClass());

        setter.setObject(stmtMock, 0, input);
        verify(stmtMock).setString(0, (String) expected);
    }
}
