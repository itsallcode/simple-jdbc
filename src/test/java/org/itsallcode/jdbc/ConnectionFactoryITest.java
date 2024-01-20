package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void createConnectionFailsUnknownDbType() {
        assertThatThrownBy(() -> connectionFactory.create("jdbc:unknown"))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error connecting to 'jdbc:unknown': No suitable driver found for jdbc:unknown");
    }

    @Test
    void createConnectionFailsUnknownUrl() {
        assertThatThrownBy(() -> connectionFactory.create("unknown"))
                .isInstanceOf(UncheckedSQLException.class)
                .hasMessage("Error connecting to 'unknown': No suitable driver found for unknown");
    }

    @Test
    void createConnectionFailsInvalidJdbcUrl() {
        assertThatThrownBy(() -> connectionFactory.create("jdbc:h2:unknown")).isInstanceOf(UncheckedSQLException.class)
                .hasMessageStartingWith(
                        "Error connecting to 'jdbc:h2:unknown': A file path that is implicitly relative to the current working directory is not allowed in the database URL");
    }
}
