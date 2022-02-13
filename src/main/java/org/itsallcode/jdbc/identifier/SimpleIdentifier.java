package org.itsallcode.jdbc.identifier;

public class SimpleIdentifier implements Identifier
{
    private final String id;

    public SimpleIdentifier(String id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return id;
    }

    @Override
    public String quote()
    {
        return "\"" + id + "\"";
    }
}
