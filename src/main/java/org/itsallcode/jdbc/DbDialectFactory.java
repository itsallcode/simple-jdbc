package org.itsallcode.jdbc;

import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.logging.Logger;

import org.itsallcode.jdbc.dialect.DbDialect;
import org.itsallcode.jdbc.dialect.GenericDialect;

class DbDialectFactory {

    private static final Logger LOG = Logger.getLogger(DbDialectFactory.class.getName());

    public DbDialect createDialect(final String url) {
        final ServiceLoader<DbDialect> serviceLoader = ServiceLoader.load(DbDialect.class,
                Thread.currentThread().getContextClassLoader());
        return serviceLoader.stream()
                .map(Provider::get)
                .filter(dialect -> dialect.supportsUrl(url))
                .findAny()
                .orElseGet(() -> {
                    LOG.warning(() -> "No dialect found for URL '%s', using generic dialect.".formatted(url));
                    return GenericDialect.INSTANCE;
                });
    }
}
