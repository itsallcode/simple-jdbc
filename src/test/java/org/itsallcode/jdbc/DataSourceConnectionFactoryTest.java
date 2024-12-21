package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.h2.jdbcx.JdbcDataSource;
import org.itsallcode.jdbc.dialect.H2Dialect;
import org.itsallcode.jdbc.resultset.generic.Row;
import org.junit.jupiter.api.Test;

class DataSourceConnectionFactoryTest {

    DataSourceConnectionFactory dataSourceWithDialectUrl() {
        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(H2TestFixture.H2_MEM_JDBC_URL);
        return DataSourceConnectionFactory.create(dataSource.getURL(), dataSource);
    }

    DataSourceConnectionFactory dataSourceWithExplicitDialect() {
        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(H2TestFixture.H2_MEM_JDBC_URL);
        return DataSourceConnectionFactory.create(new H2Dialect(), dataSource);
    }

    @Test
    void createConnection() {
        try (SimpleConnection connection = dataSourceWithDialectUrl().getConnection()) {
            assertThat(connection).isNotNull();
        }
    }

    @Test
    void connectionWorks() {
        try (SimpleConnection connection = dataSourceWithDialectUrl().getConnection()) {
            final List<Row> result = connection.query("select 1").toList();
            assertThat(result)
                    .hasSize(1)
                    .first().extracting(row -> row.get(0).getValue())
                    .isEqualTo(1);
        }
    }

    @Test
    void connectionWithDialectWorks() {
        try (SimpleConnection connection = dataSourceWithExplicitDialect().getConnection()) {
            final List<Row> result = connection.query("select 1").toList();
            assertThat(result)
                    .hasSize(1)
                    .first().extracting(row -> row.get(0).getValue())
                    .isEqualTo(1);
        }
    }
}
