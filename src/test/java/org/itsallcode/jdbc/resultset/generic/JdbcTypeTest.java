package org.itsallcode.jdbc.resultset.generic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.JDBCType;
import java.sql.Types;

import org.junit.jupiter.api.Test;

class JDBCTypeTest {
    @Test
    void typeFound() {
        assertThat(JDBCType.valueOf(Types.VARCHAR)).isEqualTo(JDBCType.VARCHAR);
    }

    @Test
    void typeNotFound() {
        assertThatThrownBy(() -> JDBCType.valueOf(Integer.MIN_VALUE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Type:-2147483648 is not a valid Types.java value.");
    }
}
