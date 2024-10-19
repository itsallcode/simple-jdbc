package org.itsallcode.jdbc.identifier;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.List;

/**
 * A qualified identifier, e.g. table name and schema name.
 * 
 * @param id list of identifiers
 */
public record QualifiedIdentifier(List<Identifier> id) implements Identifier {

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

    /**
     * Create a new qualified identifier.
     * 
     * @param ids the simple IDs
     * @return a new instance
     */
    @SuppressWarnings("java:S923") // Varargs required
    public static Identifier of(final String... ids) {
        return of(asList(ids).stream().map(Identifier::simple).toArray(Identifier[]::new));
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
