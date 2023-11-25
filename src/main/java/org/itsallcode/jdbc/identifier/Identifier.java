package org.itsallcode.jdbc.identifier;

import java.util.Arrays;

/**
 * Represents a database identifier, e.g. of a table or a schema.
 */
public interface Identifier {
    /**
     * Put the name in quotes.
     * 
     * @return quoted name
     */
    String quote();

    @Override
    String toString();

    static Identifier simple(final String id) {
        return SimpleIdentifier.of(id);
    }

    static Identifier qualified(final String... id) {
        return QualifiedIdentifier.of(Arrays.stream(id).map(SimpleIdentifier::of).toArray(SimpleIdentifier[]::new));
    }
}
