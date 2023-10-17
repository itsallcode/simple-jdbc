package org.itsallcode.jdbc.identifier;

import static java.util.stream.Collectors.joining;

import java.util.Arrays;

/**
 * A qualified identifier, e.g. table name and schema name.
 */
public class QualifiedIdentifier implements Identifier {
    private final Identifier[] id;

    private QualifiedIdentifier(final Identifier... ids) {
        this.id = ids;
    }

    /**
     * Create a new qualified identifier.
     * 
     * @param ids the IDs
     * @return a new instance
     */
    public static Identifier of(final Identifier... ids) {
        return new QualifiedIdentifier(ids);
    }

    @Override
    public String toString() {
        return quote();
    }

    @Override
    public String quote() {
        return Arrays.stream(id).map(Identifier::quote).collect(joining("."));
    }
}
