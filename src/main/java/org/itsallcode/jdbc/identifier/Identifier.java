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

    /**
     * Create a new {@link SimpleIdentifier}.
     * 
     * @param id the id
     * @return a new {@link SimpleIdentifier}
     */
    static Identifier simple(final String id) {
        return SimpleIdentifier.of(id);
    }

    /**
     * Create a new {@link QualifiedIdentifier} from the given parts.
     * 
     * @param id parts of the ID
     * @return a new {@link QualifiedIdentifier}
     */
    static Identifier qualified(final String... id) {
        return QualifiedIdentifier.of(Arrays.stream(id).map(SimpleIdentifier::of).toArray(SimpleIdentifier[]::new));
    }
}
