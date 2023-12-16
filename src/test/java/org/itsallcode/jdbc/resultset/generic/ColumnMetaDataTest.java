package org.itsallcode.jdbc.resultset.generic;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class ColumnMetaDataTest {
    @Test
    void testToString() {
        ToStringVerifier.forClass(ColumnMetaData.class).verify();
    }

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(ColumnMetaData.class).verify();
    }
}
