package org.itsallcode.jdbc.resultset.generic;

import org.junit.jupiter.api.Test;

import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;

class SimpleMetaDataTest {
    @Test
    void testToString() {
        ToStringVerifier.forClass(SimpleMetaData.class).verify();
    }

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(SimpleMetaData.class).verify();
    }
}
