package org.itsallcode.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory
{
    private final Context context;

    private ConnectionFactory(Context context)
    {
        this.context = context;
    }

    public static ConnectionFactory create()
    {
        return new ConnectionFactory(new Context());
    }

    public SimpleConnection create(String url)
    {
        return create(url, new Properties());
    }

    public SimpleConnection create(String url, Properties info)
    {
        return new SimpleConnection(createConnection(url, info), context);
    }

    private Connection createConnection(String url, Properties info)
    {
        try
        {
            return DriverManager.getConnection(url, info);
        }
        catch (final SQLException e)
        {
            throw new UncheckedSQLException("Error connecting to " + url, e);
        }
    }
}
