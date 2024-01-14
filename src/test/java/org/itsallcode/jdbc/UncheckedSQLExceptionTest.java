package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class UncheckedSQLExceptionTest {
    @Test
    void create() {
        assertThat(new UncheckedSQLException(new SQLException("mock")))
                .hasToString("org.itsallcode.jdbc.UncheckedSQLException: java.sql.SQLException: mock");
    }

    @Test
    void createWithMessage() {
        assertThat(new UncheckedSQLException("msg", new SQLException("mock")))
                .hasToString("org.itsallcode.jdbc.UncheckedSQLException: msg");
    }
}
