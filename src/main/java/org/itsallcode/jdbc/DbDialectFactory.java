package org.itsallcode.jdbc;

import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import org.itsallcode.jdbc.dialect.DbDialect;

class DbDialectFactory {
    public DbDialect createDialect(final String url) {
        final ServiceLoader<DbDialect> serviceLoader = ServiceLoader.load(DbDialect.class,
                Thread.currentThread().getContextClassLoader());
        return serviceLoader.stream()
                .map(Provider::get)
                .filter(dialect -> dialect.supportsUrl(url))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("No DB dialect registered for JDBC URL '" + url + "'"));
    }
}
