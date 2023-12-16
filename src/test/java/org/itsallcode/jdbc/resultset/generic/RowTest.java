package org.itsallcode.jdbc.resultset.generic;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class RowTest {
    @Test
    void testToString() {
        ToStringVerifier.forClass(Row.class).verify();
    }

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(Row.class).verify();
    }
}
