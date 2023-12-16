package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import org.itsallcode.jdbc.dialect.DbDialect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConnectionFactoryITest {
    private ConnectionFactory connectionFactory;

    @BeforeEach
    void setUp() {
        connectionFactory = ConnectionFactory.create(Context.builder().dialect(DbDialect.h2()).build());
    }

    @Test
    void createConnection() {
        try (SimpleConnection connection = connectionFactory.create("jdbc:h2:mem:")) {
            assertThat(connection).isNotNull();
        }
    }
}
