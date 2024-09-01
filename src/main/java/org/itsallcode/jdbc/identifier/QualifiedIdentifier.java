package org.itsallcode.jdbc.identifier;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

/**
 * A qualified identifier, e.g. table name and schema name.
 */
record QualifiedIdentifier(List<Identifier> id) implements Identifier {

    /**
     * Create a new qualified identifier.
     * 
     * @param ids the IDs
     * @return a new instance
     */
    @SuppressWarnings("java:S923") // Varargs required
    public static Identifier of(final Identifier... ids) {
        return new QualifiedIdentifier(asList(ids));
    }

    @Override
    public String toString() {
        return quote();
    }

    @Override
    public String quote() {
        return id.stream().map(Identifier::quote).collect(joining("."));
    }
}
