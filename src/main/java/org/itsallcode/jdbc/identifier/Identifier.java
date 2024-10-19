package org.itsallcode.jdbc.identifier;

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
}
