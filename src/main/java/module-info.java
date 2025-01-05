/**
 * <a href="https://github.com/itsallcode/simple-jdbc">Simple-JDBC</a> is a
 * wrapper for the JDBC API that simplifies the use of JDBC.
 * <ul>
 * <li>Create a {@link org.itsallcode.jdbc.SimpleConnection}
 * <ul>
 * <li>... with a connection factory using {@link java.sql.DriverManager}, see
 * {@link org.itsallcode.jdbc.DataSourceConnectionFactory}</li>
 * <li>... with a {@link javax.sql.DataSource}, see
 * {@link org.itsallcode.jdbc.DataSourceConnectionFactory}</li>
 * <li>... with an existing {@link java.sql.Connection}, see
 * {@link org.itsallcode.jdbc.SimpleConnection#wrap(Connection, DbDialect)}</li>
 * </ul>
 * </li>
 * <li>Execute statements
 * <ul>
 * <li>... single statement:
 * {@link org.itsallcode.jdbc.DbOperations#executeUpdate(String)}</li>
 * <li>... with a prepared statement and generic parameters:
 * {@link org.itsallcode.jdbc.DbOperations#executeUpdate(String, List)}</li>
 * <li>... with a prepared statement and custom parameter setter:
 * {@link org.itsallcode.jdbc.DbOperations#executeUpdate(String, org.itsallcode.jdbc.PreparedStatementSetter)}</li>
 * <li>... multiple statements in a batch:
 * {@link org.itsallcode.jdbc.DbOperations#batch()}</li>
 * 
 * <li>... semicolon separated SQL script:
 * {@link org.itsallcode.jdbc.DbOperations#executeScript(String)}</li>
 * </ul>
 * <li>Execute queries
 * <ul>
 * <li>...with generic result types:
 * {@link org.itsallcode.jdbc.DbOperations#query(String)}</li>
 * <li>...with a {@link org.itsallcode.jdbc.resultset.RowMapper}, returning
 * custom result types:
 * {@link org.itsallcode.jdbc.DbOperations#query(String, org.itsallcode.jdbc.resultset.RowMapper)}</li>
 * <li>...with a prepared statement and generic parameters:
 * {@link org.itsallcode.jdbc.DbOperations#query(String, List, org.itsallcode.jdbc.resultset.RowMapper)}</li>
 * <li>...with a prepared statement and custom parameter setter:
 * {@link org.itsallcode.jdbc.DbOperations#query(String, org.itsallcode.jdbc.PreparedStatementSetter, org.itsallcode.jdbc.resultset.RowMapper)}</li>
 * </ul>
 * </li>
 * <li>Use transactions:
 * <ul>
 * <li>Start a new transaction:
 * {@link org.itsallcode.jdbc.SimpleConnection#startTransaction()}</li>
 * <li>Use all operations from {@link org.itsallcode.jdbc.DbOperations} in a
 * transaction</li>
 * <li>Rollback a transaction:
 * {@link org.itsallcode.jdbc.Transaction#rollback()}</li>
 * <li>Commit a transaction:
 * {@link org.itsallcode.jdbc.Transaction#commit()}</li>
 * <li>Automatic rollback using try-with-resources if not committed:
 * {@link org.itsallcode.jdbc.Transaction#close()}</li>
 * </ul>
 * </li>
 * <li>Batch inserts
 * <ul>
 * <li>... using a {@link java.util.stream.Stream} or {@link java.util.Iterator}
 * of row objects:
 * {@link org.itsallcode.jdbc.SimpleConnection#batchInsert(java.lang.Class)}</li>
 * <li>... directly setting values of a {@link java.sql.PreparedStatement}:
 * {@link org.itsallcode.jdbc.SimpleConnection#batchInsert()}</li>
 * </ul>
 * </li>
 * <li>Simplified Exception Handling: converts checked exception
 * {@link java.sql.SQLException} to runtime exception
 * {@link org.itsallcode.jdbc.UncheckedSQLException}</li>
 * </ul>
 */
module org.itsallcode.jdbc {
    exports org.itsallcode.jdbc;
    exports org.itsallcode.jdbc.batch;
    exports org.itsallcode.jdbc.identifier;
    exports org.itsallcode.jdbc.resultset;
    exports org.itsallcode.jdbc.resultset.generic;
    exports org.itsallcode.jdbc.dialect;

    requires java.logging;
    requires transitive java.sql;

    uses org.itsallcode.jdbc.dialect.DbDialect;

    provides org.itsallcode.jdbc.dialect.DbDialect
            with org.itsallcode.jdbc.dialect.ExasolDialect, org.itsallcode.jdbc.dialect.H2Dialect;
}
