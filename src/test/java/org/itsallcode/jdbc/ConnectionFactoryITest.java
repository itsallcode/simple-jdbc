package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConnectionFactoryITest {
    private ConnectionFactory connectionFactory;

    @BeforeEach
    void setUp() {
        connectionFactory = ConnectionFactory.create(Context.builder().build());
    }

    @Test
    void createConnection() {
        try (SimpleConnection connection = connectionFactory.create("jdbc:h2:mem:")) {
            assertThat(connection).isNotNull();
        }
    }
}
