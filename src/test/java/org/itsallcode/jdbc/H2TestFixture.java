package org.itsallcode.jdbc;

public class H2TestFixture {
    public static SimpleConnection createMemConnection() {
        return ConnectionFactory.create().create("jdbc:h2:mem:");
    }

    public static SimpleConnection createMemConnectionWithModernTypes() {
        return ConnectionFactory.create(Context.builder().useModernTypes(true).build()).create("jdbc:h2:mem:");
    }
}
