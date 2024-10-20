package org.itsallcode.jdbc.statement;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import org.itsallcode.jdbc.dialect.ColumnValueSetter;
import org.itsallcode.jdbc.dialect.DbDialect;

/**
 * This class creates and caches {@link ColumnValueSetter} based on an object's
 * type.
 */
public class ParamSetterProvider {
    private static final ColumnValueSetter<Object> GENERIC_SETTER = PreparedStatement::setObject;
    private final DbDialect dialect;
    private final Map<String, ColumnValueSetter<Object>> setters = new HashMap<>();

    /**
     * Create a new instance.
     * 
     * @param dialect database dialect
     */
    public ParamSetterProvider(final DbDialect dialect) {
        this.dialect = dialect;
    }

    @SuppressWarnings("unchecked")
    ColumnValueSetter<Object> findSetter(final Object object) {
        if (object == null) {
            return GENERIC_SETTER;
        }
        return setters.computeIfAbsent(object.getClass().getName(),
                type -> (ColumnValueSetter<Object>) dialect.createSetter(object.getClass()));
    }
}
