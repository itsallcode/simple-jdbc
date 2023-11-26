package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.itsallcode.jdbc.resultset.Row;
import org.itsallcode.jdbc.resultset.SimpleResultSet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.containers.ExasolContainer;
import com.exasol.containers.ExasolService;

class LegacyTypeITest {

    private static final ExasolContainer<?> container = new ExasolContainer<>("8.23.1")
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
        return ConnectionFactory.create(Context.builder().useModernTypes(true).build()).create(container.getJdbcUrl(),
                container.getUsername(), container.getPassword());
    }

    @ParameterizedTest
    @MethodSource("testTypes")
    void type(final TypeTest test) {
        try (SimpleResultSet<Row> result = connect()
                .query("select cast('" + test.value() + "' as " + test.type() + ")")) {
            final Object value = result.toList().get(0).getColumnValue(0).getValue();
            assertAll(
                    () -> assertThat(value.getClass()).isEqualTo(test.expectedValue().getClass()),
                    () -> assertThat(value).isEqualTo(test.expectedValue()));
        }
    }

    @ParameterizedTest
    @MethodSource("testTypes")
    void nullValue(final TypeTest test) {
        try (SimpleResultSet<Row> result = connect()
                .query("select cast(NULL as " + test.type() + ")")) {
            assertThat(result.toList().get(0).getColumnValue(0).getValue())
                    .isNull();
        }
    }

    static Stream<Arguments> testTypes() {
        return Stream.of(
                typeTest("2023-11-25 16:18:46", "timestamp", Instant.parse("2023-11-25T16:18:46.0Z")),
                typeTest("2023-11-25", "date", LocalDate.parse("2023-11-25")),
                typeTest("5-3", "INTERVAL YEAR TO MONTH", "+05-03"),
                typeTest("2 12:50:10.123", "INTERVAL DAY TO SECOND", "+02 12:50:10.123"),
                typeTest("POINT(1 2)", "GEOMETRY", "POINT (1 2)"),
                typeTest("550e8400-e29b-11d4-a716-446655440000", "HASHTYPE", "550e8400e29b11d4a716446655440000"),
                typeTest("text", "VARCHAR(10)", "text"),
                typeTest("text", "CHAR(10)", "text      "),
                typeTest("123.456", "DECIMAL", 123L),
                typeTest("123.457", "DECIMAL(6,3)", BigDecimal.valueOf(123.457d)),
                typeTest("123.458", "DOUBLE PRECISION", 123.458d),
                typeTest("true", "BOOLEAN", true));
    }

    private static Arguments typeTest(final String value, final String type, final Object expectedValue) {
        return Arguments.of(new TypeTest(value, type, expectedValue));
    }

    record TypeTest(String value, String type, Object expectedValue) {

    }
}
