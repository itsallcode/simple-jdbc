package org.itsallcode.jdbc.identifier;

/**
 * An identifier consisting only of an ID.
 */
record SimpleIdentifier(String id) implements Identifier {

    /**
     * Create a new simple identifier.
     * 
     * @param id the ID
     * @return a new instance
     */
    public static Identifier of(final String id) {
        return new SimpleIdentifier(id);
    }

    @Override
    public String toString() {
        return quote();
    }

    @Override
    public String quote() {
        return "\"" + id + "\"";
    }
}
