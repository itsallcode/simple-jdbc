package org.itsallcode.jdbc.identifier;

import static java.util.stream.Collectors.joining;

import java.util.Arrays;

public class QualifiedIdentifier implements Identifier
{
    private final Identifier[] id;

    public QualifiedIdentifier(Identifier... id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return Arrays.stream(id).map(Identifier::toString).collect(joining("."));
    }

    @Override
    public String quote()
    {
        return Arrays.stream(id).map(Identifier::quote).collect(joining("."));
    }
}
