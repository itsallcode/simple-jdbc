package org.itsallcode.jdbc;

import org.itsallcode.jdbc.dialect.H2Dialect;

public class H2TestFixture {
    public static SimpleConnection createMemConnection() {
        return createMemConnection(Context.builder().dialect(new H2Dialect()).build());
    }

    public static SimpleConnection createMemConnection(final Context context) {
        return ConnectionFactory.create(context).create("jdbc:h2:mem:");
    }
}
