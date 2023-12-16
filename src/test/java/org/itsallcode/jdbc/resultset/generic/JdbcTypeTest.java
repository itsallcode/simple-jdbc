package org.itsallcode.jdbc.resultset.generic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.Types;

import org.junit.jupiter.api.Test;

class JdbcTypeTest {
    @Test
    void typeFound() {
        assertThat(JdbcType.forType(Types.VARCHAR)).isEqualTo(JdbcType.VARCHAR);
    }

    @Test
    void typeNotFound() {
        assertThatThrownBy(() -> JdbcType.forType(Integer.MIN_VALUE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No JDBC type found for value -2147483648");
    }
}
