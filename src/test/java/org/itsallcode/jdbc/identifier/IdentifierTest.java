package org.itsallcode.jdbc.identifier;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class IdentifierTest {
    @Test
    void simple() {
        final Identifier id = Identifier.simple("id");
        assertThat(id).isNotNull().hasToString("\"id\"");
    }
}
