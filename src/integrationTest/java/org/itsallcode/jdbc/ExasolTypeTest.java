package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.time.Instant;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.itsallcode.jdbc.resultset.generic.ColumnValue;
import org.itsallcode.jdbc.resultset.generic.Row;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.containers.ExasolContainer;
import com.exasol.containers.ExasolService;

class ExasolTypeTest {

    private static final ExasolContainer<?> container = new ExasolContainer<>("8.24.0")
            .withRequiredServices(ExasolService.JDBC).withReuse(true);

    @BeforeAll
    static void startDb() {
        container.start();
    }

    @AfterAll
    static void stopDb() {
        container.stop();
    }

    SimpleConnection connect() {
        return ConnectionFactory.create(Context.builder().build()) //
                .create(container.getJdbcUrl(), container.getUsername(), container.getPassword());
    }

    @ParameterizedTest
    @MethodSource("testTypes")
    void genericRowType(final TypeTest test) {
        try (SimpleResultSet<Row> result = connect()
                .query("select cast('" + test.value() + "' as " + test.type() + ")")) {
            final ColumnValue columnValue = result.toList().get(0).get(0);
            final Object value = columnValue.value();
            assertAll(
                    () -> assertThat(value.getClass()).isEqualTo(test.expectedValue().getClass()),
                    () -> assertThat(value).isEqualTo(test.expectedValue()),
                    () -> assertThat(columnValue.type().jdbcType()).isEqualTo(test.expectedType()));
        }
    }

    @ParameterizedTest
    @MethodSource("testTypes")
    void genericRowNullValue(final TypeTest test) {
        try (SimpleResultSet<Row> result = connect()
                .query("select cast(NULL as " + test.type() + ")")) {
            final ColumnValue value = result.toList().get(0).get(0);
            assertAll(
                    () -> assertThat(value.value()).isNull(),
                    () -> assertThat(value.type().jdbcType()).isEqualTo(test.expectedType()));
        }
    }

    @ParameterizedTest
    @MethodSource("testTypes")
    void resultSetValueTypes(final TypeTest test) {
        try (SimpleConnection connection = connect();
                SimpleResultSet<Object> result = connection
                        .query("select cast('" + test.value() + "' as " + test.type() + ")",
                                (resultSet, rowNum) -> resultSet.getObject(1,
                                        test.expectedValue().getClass()))) {
            final Object value = result.toList().get(0);
            assertAll(
                    () -> assertThat(value.getClass()).isEqualTo(test.expectedValue().getClass()),
                    () -> assertThat(value).isEqualTo(test.expectedValue()));
        }
    }

    static Stream<Arguments> testTypes() {
        return Stream.of(
                typeTest("2023-11-25 16:18:46", "timestamp", Instant.parse("2023-11-25T16:18:46.0Z"),
                        JDBCType.TIMESTAMP),
                typeTest("2023-11-25", "date", LocalDate.parse("2023-11-25"), JDBCType.DATE),
                typeTest("5-3", "INTERVAL YEAR TO MONTH", "+05-03", JDBCType.VARCHAR),
                typeTest("2 12:50:10.123", "INTERVAL DAY TO SECOND", "+02 12:50:10.123", JDBCType.VARCHAR),
                typeTest("POINT(1 2)", "GEOMETRY", "POINT (1 2)", JDBCType.VARCHAR),
                typeTest("550e8400-e29b-11d4-a716-446655440000", "HASHTYPE", "550e8400e29b11d4a716446655440000",
                        JDBCType.CHAR),
                typeTest("text", "VARCHAR(10)", "text", JDBCType.VARCHAR),
                typeTest("text", "CHAR(10)", "text      ", JDBCType.CHAR),
                typeTest("123.456", "DECIMAL", 123L, JDBCType.BIGINT),
                typeTest("123", "SHORTINT", 123, JDBCType.INTEGER),
                typeTest("123", "SMALLINT", 123, JDBCType.INTEGER),
                typeTest("123", "TINYINT", (short) 123, JDBCType.SMALLINT),
                typeTest("123", "DECIMAL(4,0)", (short) 123, JDBCType.SMALLINT),
                typeTest("123", "DECIMAL(5,0)", 123, JDBCType.INTEGER),
                typeTest("123", "DECIMAL(9,0)", 123, JDBCType.INTEGER),
                typeTest("123", "DECIMAL(10,0)", 123L, JDBCType.BIGINT),
                typeTest("123", "DECIMAL(18,0)", 123L, JDBCType.BIGINT),
                typeTest("123", "DECIMAL(19,0)", BigDecimal.valueOf(123), JDBCType.DECIMAL),
                typeTest("123", "INT", 123L, JDBCType.BIGINT),
                typeTest("123", "BIGINT", BigDecimal.valueOf(123), JDBCType.DECIMAL),
                typeTest("123", "DEC", 123L, JDBCType.BIGINT),
                typeTest("123", "NUMERIC", 123L, JDBCType.BIGINT),
                typeTest("123.457", "DECIMAL(6,3)", BigDecimal.valueOf(123.457d), JDBCType.DECIMAL),
                typeTest("123.458", "DOUBLE", 123.458d, JDBCType.DOUBLE),
                typeTest("123.458", "FLOAT", 123.458d, JDBCType.DOUBLE),
                typeTest("123.458", "NUMBER", 123.458d, JDBCType.DOUBLE),
                typeTest("123.458", "REAL", 123.458d, JDBCType.DOUBLE),
                typeTest("123.458", "DOUBLE PRECISION", 123.458d, JDBCType.DOUBLE),
                typeTest("true", "BOOLEAN", true, JDBCType.BOOLEAN));
    }

    private static Arguments typeTest(final String value, final String type, final Object expectedValue,
            final JDBCType expectedType) {
        return Arguments.of(new TypeTest(value, type, expectedValue, expectedType));
    }

    record TypeTest(String value, String type, Object expectedValue, JDBCType expectedType) {

    }
}
