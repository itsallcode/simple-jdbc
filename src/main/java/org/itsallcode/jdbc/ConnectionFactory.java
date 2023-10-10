package org.itsallcode.jdbc;

import java.sql.*;
import java.util.Properties;

public class ConnectionFactory {
    private final Context context;

    private ConnectionFactory(final Context context) {
        this.context = context;
    }

    public static ConnectionFactory create() {
        return new ConnectionFactory(new Context());
    }

    public SimpleConnection create(final String url) {
        return create(url, new Properties());
    }

    public SimpleConnection create(final String url, final String user, final String password) {
        final Properties info = new Properties();
        info.put("user", user);
        info.put("password", password);
        return create(url, info);
    }

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
