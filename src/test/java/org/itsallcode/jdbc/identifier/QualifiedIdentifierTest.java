package org.itsallcode.jdbc.identifier;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class QualifiedIdentifierTest {
    @Test
    void testToString() {
        assertThat(QualifiedIdentifier.of(SimpleIdentifier.of("id"))).hasToString("\"id\"");
    }

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(QualifiedIdentifier.class).verify();
    }
}
