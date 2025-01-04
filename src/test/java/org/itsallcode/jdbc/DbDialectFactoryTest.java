package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import org.itsallcode.jdbc.dialect.*;
import org.junit.jupiter.api.Test;

class DbDialectFactoryTest {
    @Test
    void exasolDialect() {
        assertThat(new DbDialectFactory().createDialect("jdbc:exa:localhost:8563;schema=SCHEMA"))
                .isInstanceOf(ExasolDialect.class);
    }

    @Test
    void h2Dialect() {
        assertThat(new DbDialectFactory().createDialect("jdbc:h2:mem:"))
                .isInstanceOf(H2Dialect.class);
    }

    @Test
    void genericDialect() {
        assertThat(new DbDialectFactory().createDialect("jdbc:unknown:localhost:8563;schema=SCHEMA"))
                .isInstanceOf(GenericDialect.class);
    }
}
