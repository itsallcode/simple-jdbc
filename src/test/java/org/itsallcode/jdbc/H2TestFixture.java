package org.itsallcode.jdbc;

import org.itsallcode.jdbc.dialect.DbDialect;

public class H2TestFixture {
    public static SimpleConnection createMemConnection() {
        return createMemConnection(Context.builder().dialect(DbDialect.h2()).build());
    }

    public static SimpleConnection createMemConnection(final Context context) {
        return ConnectionFactory.create(context).create("jdbc:h2:mem:");
    }
}
