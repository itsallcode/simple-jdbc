package org.itsallcode.jdbc.identifier;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class SimpleIdentifierTest {
    @Test
    void testToString() {
        assertThat(SimpleIdentifier.of("id")).hasToString("\"id\"");
    }

    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(SimpleIdentifier.class).verify();
    }
}
