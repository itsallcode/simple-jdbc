package org.itsallcode.jdbc.dialect;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.h2.jdbcx.JdbcDataSource;
import org.itsallcode.jdbc.*;
import org.itsallcode.jdbc.resultset.ContextRowMapper;
import org.itsallcode.jdbc.resultset.generic.Row;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GenericDialectITest {

    static Stream<Arguments> types() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of("a"),
                Arguments.of(1.0),
                Arguments.of(true),
                Arguments.of((Object) null));
    }

    @ParameterizedTest
    @MethodSource("types")
    void setGet(final Object value) {
        try (SimpleConnection connection = genericDialectConnection()) {
            final List<Row> result = connection
                    .query("select ?", asList(value), ContextRowMapper.generic(GenericDialect.INSTANCE)).toList();
            assertThat(result)
                    .hasSize(1)
                    .first().extracting(row -> row.get(0).getValue())
                    .isEqualTo(value);
        }
    }

    private SimpleConnection genericDialectConnection() {
        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(H2TestFixture.H2_MEM_JDBC_URL);
        return DataSourceConnectionFactory.create(GenericDialect.INSTANCE, dataSource).getConnection();
    }
}
