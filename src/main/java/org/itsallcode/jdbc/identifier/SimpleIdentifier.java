package org.itsallcode.jdbc.identifier;

public class SimpleIdentifier implements Identifier {
    private final String id;

    private SimpleIdentifier(String id) {
        this.id = id;
    }

    public static Identifier of(String id) {
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
