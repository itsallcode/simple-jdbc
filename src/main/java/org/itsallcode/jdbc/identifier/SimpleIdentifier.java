package org.itsallcode.jdbc.identifier;

/**
 * An identifier consisting only of an ID.
 */
public class SimpleIdentifier implements Identifier {
    private final String id;

    private SimpleIdentifier(final String id) {
        this.id = id;
    }

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
