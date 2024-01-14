package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;
import java.util.stream.Stream;

import org.h2.api.Interval;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.itsallcode.jdbc.resultset.generic.Row;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class H2TypeTest {
    @ParameterizedTest
    @MethodSource("testTypes")
    void genericRowValueTypes(final TypeTest test) {
        try (SimpleConnection connection = H2TestFixture.createMemConnection();
                SimpleResultSet<Row> result = connection
                        .query("select cast('" + test.value() + "' as " + test.type() + ")")) {
            final Object value = result.toList().get(0).get(0).value();
            assertAll(
                    () -> assertThat(value.getClass()).isEqualTo(test.expectedValue().getClass()),
                    () -> assertThat(value).isEqualTo(test.expectedValue()));
        }
    }

    @ParameterizedTest
    @MethodSource("testTypes")
    void genericRowNullTypes(final TypeTest test) {
        try (SimpleConnection connection = H2TestFixture.createMemConnection();
                SimpleResultSet<Row> result = connection
                        .query("select cast(NULL as " + test.type() + ")")) {
            final Object value = result.toList().get(0).get(0).value();
            assertThat(value).isNull();
        }
    }

    @ParameterizedTest
    @MethodSource("testTypes")
    void resultSetValueTypes(final TypeTest test) {
        try (SimpleConnection connection = H2TestFixture.createMemConnection();
                SimpleResultSet<Object> result = connection
                        .query("select cast('" + test.value() + "' as " + test.type() + ")",
                                (resultSet, rowNum) -> resultSet
                                        .getObject(1,
                                                test.expectedValue()
                                                        .getClass()))) {
            final Object value = result.toList().get(0);
            assertAll(
                    () -> assertThat(value.getClass()).isEqualTo(test.expectedValue().getClass()),
                    () -> assertThat(value).isEqualTo(test.expectedValue()));
        }
    }

    @ParameterizedTest
    @MethodSource("testTypes")
    void resultSetValueTypesNull(final TypeTest test) {
        try (SimpleConnection connection = H2TestFixture.createMemConnection();
                SimpleResultSet<Object> result = connection
                        .query("select cast(NULL as " + test.type() + ")",
                                (resultSet, rowNum) -> resultSet
                                        .getObject(1,
                                                test.expectedValue()
                                                        .getClass()))) {
            final Object value = result.toList().get(0);
            assertThat(value).isNull();
        }
    }

    static Stream<Arguments> testTypes() {
        return Stream.of(
                typeTest("text", "CHARACTER(5)", "text "),
                typeTest("text", "CHARACTER VARYING", "text"),
                typeTest("text", "CHARACTER LARGE OBJECT", "text"),
                typeTest("text", "VARCHAR_IGNORECASE", "text"),
                typeTest("text", "BINARY(5)", new byte[] { 116, 101, 120, 116, 0 }),
                typeTest("text", "BINARY VARYING", new byte[] { 116, 101, 120, 116 }),
                typeTest("text", "BINARY LARGE OBJECT", new byte[] { 116, 101, 120, 116 }),
                typeTest("true", "BOOLEAN", true),
                typeTest("42", "TINYINT", 42),
                typeTest("42", "SMALLINT", 42),
                typeTest("42", "INTEGER", 42),
                typeTest("42", "BIGINT", 42L),
                typeTest("42", "NUMERIC", BigDecimal.valueOf(42)),
                typeTest("42.13", "REAL", 42.13f),
                typeTest("123.458", "DOUBLE PRECISION", 123.458d),
                typeTest("123.458", "DECFLOAT", BigDecimal.valueOf(123.458d)),

                typeTest("2023-11-25", "date", LocalDate.parse("2023-11-25")),
                typeTest("13:24:40", "TIME", LocalTime.parse("13:24:40")),
                typeTest("2023-11-25 16:18:46", "timestamp", Instant.parse("2023-11-25T16:18:46.0Z")),

                typeTest("10", "INTERVAL YEAR", Interval.ofYears(10)),
                typeTest("a", "ENUM('a', 'b')", "a"),
                typeTest("POINT(1 2)", "GEOMETRY", "POINT (1 2)"),
                typeTest("{\"key\":42}", "JSON",
                        new byte[] { 34, 123, 92, 34, 107, 101, 121, 92, 34, 58, 52, 50, 125,
                                34 }),
                typeTest("550e8400-e29b-11d4-a716-446655440000", "UUID",
                        UUID.fromString("550e8400-e29b-11d4-a716-446655440000"))

        );
    }

    private static Arguments typeTest(final String value, final String type, final Object expectedValue) {
        return Arguments.of(new TypeTest(value, type, expectedValue));
    }

    record TypeTest(String value, String type, Object expectedValue) {
    }
}
