package org.itsallcode.jdbc;

public class H2TestFixture {
    public static SimpleConnection createMemConnection() {
        return createMemConnection(Context.builder().build());
    }

    public static SimpleConnection createMemConnectionWithModernTypes() {
        return createMemConnection(Context.builder().useModernTypes(true).build());
    }

    public static SimpleConnection createMemConnection(final Context context) {
        return ConnectionFactory.create(context).create("jdbc:h2:mem:");
    }
}
