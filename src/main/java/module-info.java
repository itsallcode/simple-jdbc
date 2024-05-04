/**
 * Module containing a simple wrapper for the JDBC API.
 */

module org.itsallcode.jdbc {
    exports org.itsallcode.jdbc;
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
