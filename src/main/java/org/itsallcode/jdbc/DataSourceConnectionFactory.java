package org.itsallcode.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.itsallcode.jdbc.dialect.DbDialect;

/**
 * This class connects to a database using a {@link DataSource} and returns new
 * {@link SimpleConnection}s.
 * <p>
 * Create a new instance using {@link #create(String, DataSource)} or
 * {@link #create(DbDialect, DataSource)}.
 */
public final class DataSourceConnectionFactory {
    private final Context context;
    private final DbDialect dialect;
    private final DataSource dataSource;

    private DataSourceConnectionFactory(final Context context, final DbDialect dialect, final DataSource dataSource) {
        this.context = context;
        this.dialect = dialect;
        this.dataSource = dataSource;
    }

    /**
     * Create a new {@link DataSourceConnectionFactory} using the given JDBC URL and
     * data source.
     * <p>
     * Note: The data source will not be closed automatically. It is the caller's
     * responsibility to close the data source.
     * 
     * @param jdbcUrl    JDBC URL used to determine the database dialect
     * @param dataSource data source
     * @return new connection factory
     */
    public static DataSourceConnectionFactory create(final String jdbcUrl, final DataSource dataSource) {
        final DbDialect dialect = new DbDialectFactory().createDialect(jdbcUrl);
        return create(dialect, dataSource);
    }

    /**
     * Create a new {@link DataSourceConnectionFactory} using the given dialect and
     * data source.
     * <p>
     * Note: The data source will not be closed automatically. It is the caller's
     * responsibility to close the data source.
     * 
     * @param dialect    database dialect
     * @param dataSource data source
     * @return new connection factory
     */
    public static DataSourceConnectionFactory create(final DbDialect dialect, final DataSource dataSource) {
        return new DataSourceConnectionFactory(Context.builder().build(), dialect, dataSource);
    }

    /**
     * Get a new {@link SimpleConnection} from the {@link DataSource}.
     * 
     * @return new connection
     */
    public SimpleConnection getConnection() {
        return new SimpleConnection(createConnection(), context, dialect);
    }

    private Connection createConnection() {
        try {
            return dataSource.getConnection();
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error getting connection from data source", e);
        }
    }
}
