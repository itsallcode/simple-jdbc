package org.itsallcode.jdbc.resultset.generic;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class ColumnValueTest {
    @Test
    void testToString() {
        ToStringVerifier.forClass(ColumnValue.class).verify();
    }

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ColumnValue.class).verify();
    }
}
