package org.itsallcode.jdbc;

public class H2TestFixture
{
    private static final ConnectionFactory connectionFactory = ConnectionFactory.create();

    public static SimpleConnection createMemConnection()
    {
        return connectionFactory.create("jdbc:h2:mem:");
    }
}
