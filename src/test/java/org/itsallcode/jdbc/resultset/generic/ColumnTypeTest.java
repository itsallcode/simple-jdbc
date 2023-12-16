package org.itsallcode.jdbc.resultset.generic;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class ColumnTypeTest {
    @Test
    void testToString() {
        ToStringVerifier.forClass(ColumnType.class).verify();
    }

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ColumnType.class).verify();
    }
}
