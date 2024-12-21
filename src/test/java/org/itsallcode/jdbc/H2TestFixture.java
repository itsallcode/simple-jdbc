package org.itsallcode.jdbc;

public class H2TestFixture {
    public static final String H2_MEM_JDBC_URL = "jdbc:h2:mem:";

    public static SimpleConnection createMemConnection() {
        return createMemConnection(Context.builder().build());
    }

    private static SimpleConnection createMemConnection(final Context context) {
        return ConnectionFactory.create(context).create(H2_MEM_JDBC_URL);
    }
}
