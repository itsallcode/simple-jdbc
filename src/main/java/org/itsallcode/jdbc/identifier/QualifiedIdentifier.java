package org.itsallcode.jdbc.identifier;

import static java.util.stream.Collectors.joining;

import java.util.Arrays;

public class QualifiedIdentifier implements Identifier {
    private final Identifier[] id;

    private QualifiedIdentifier(Identifier... ids) {
        this.id = ids;
    }

    public static Identifier of(Identifier... ids) {
        return new QualifiedIdentifier(ids);
    }

    @Override
    public String toString() {
        return Arrays.stream(id).map(Identifier::toString).collect(joining("."));
    }

    @Override
    public String quote() {
        return Arrays.stream(id).map(Identifier::quote).collect(joining("."));
    }
}
