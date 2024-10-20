package org.itsallcode.jdbc.statement;

import java.util.HashMap;
import java.util.Map;

import org.itsallcode.jdbc.dialect.*;

public class ParamSetterProvider {
    private static final ColumnValueSetter<Object> GENERIC_SETTER = Setters.generic();
    private final DbDialect dialect;
    private final Map<Class<?>, ColumnValueSetter<Object>> setters = new HashMap<>();

    public ParamSetterProvider(final DbDialect dialect) {
        this.dialect = dialect;
    }

    @SuppressWarnings("unchecked")
    ColumnValueSetter<Object> findSetter(final Object object) {
        if (object == null) {
            return GENERIC_SETTER;
        }
        return setters.computeIfAbsent(object.getClass(),
                type -> (ColumnValueSetter<Object>) dialect.createSetter(type));
    }
}
