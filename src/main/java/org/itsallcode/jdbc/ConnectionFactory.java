package org.itsallcode.jdbc;

import java.sql.*;
import java.util.Properties;

/**
 * This class connects to a database and returns new {@link SimpleConnection}s.
 */
public class ConnectionFactory {
    private final Context context;

    private ConnectionFactory(final Context context) {
        this.context = context;
    }

    /**
     * Create a new connection factory with a custom context.
     * 
     * @param context a custom context
     * @return a new instance
     */
    public static ConnectionFactory create(final Context context) {
        return new ConnectionFactory(context);
    }

    /**
     * Create a connection using the given JDBC URL.
     * 
     * @param url JDBC URL
     * @return a new connection
     */
    public SimpleConnection create(final String url) {
        return create(url, new Properties());
    }

    /**
     * Create a connection using the given JDBC URL.
     * 
     * @param url      JDBC URL
     * @param user     database user
     * @param password database password
     * @return a new connection
     */
    public SimpleConnection create(final String url, final String user, final String password) {
        final Properties info = new Properties();
        info.put("user", user);
        info.put("password", password);
        return create(url, info);
    }

    /**
     * Create a connection using the given JDBC URL.
     * 
     * @param url  JDBC URL
     * @param info connection properties
     * @return a new connection
     */
    public SimpleConnection create(final String url, final Properties info) {
        return new SimpleConnection(createConnection(url, info), context);
    }

    private Connection createConnection(final String url, final Properties info) {
        try {
            return DriverManager.getConnection(url, info);
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error connecting to '" + url + "'", e);
        }
    }
}
