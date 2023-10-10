package org.itsallcode.jdbc;

import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.util.*;

public class SimpleParameterMetaData {

    private final ParameterMetaData metaData;

    SimpleParameterMetaData(final ParameterMetaData parameterMetaData) {
        this.metaData = parameterMetaData;
    }

    public List<Parameter> getParameters() {
        try {
            final List<Parameter> parameters = new ArrayList<>(metaData.getParameterCount());
            for (int i = 1; i <= metaData.getParameterCount(); i++) {
                parameters.add(new Parameter(metaData.getParameterClassName(i), metaData.getParameterType(i),
                        metaData.getParameterTypeName(i), ParameterMode.of(metaData.getParameterMode(i)),
                        metaData.getPrecision(i), metaData.getScale(i), metaData.isSigned(i),
                        ParameterNullable.of(metaData.isNullable(i))));
            }
            return parameters;
        } catch (final SQLException e) {
            throw new UncheckedSQLException("Error getting parameter metadata", e);
        }
    }

    public enum ParameterMode {
        IN(ParameterMetaData.parameterModeIn),
        INOUT(ParameterMetaData.parameterModeInOut),
        OUT(ParameterMetaData.parameterModeOut),
        UNKNWON(ParameterMetaData.parameterModeUnknown);

        private final int mode;

        private ParameterMode(final int mode) {
            this.mode = mode;
        }

        public static ParameterMode of(final int mode) {
            return Arrays.stream(values())
                    .filter(m -> m.mode == mode)
                    .findAny()
                    .orElseThrow(
                            () -> new IllegalArgumentException("No parameter mode found for value " + mode));
        }
    }

    public enum ParameterNullable {
        NO_NULLS(ParameterMetaData.parameterNoNulls),
        NULLABLE(ParameterMetaData.parameterNullable),
        UNKNWON(ParameterMetaData.parameterNullableUnknown);

        private final int mode;

        private ParameterNullable(final int mode) {
            this.mode = mode;
        }

        public static ParameterNullable of(final int mode) {
            return Arrays.stream(values()).filter(m -> m.mode == mode).findAny().orElseThrow(
                    () -> new IllegalArgumentException("No parameter mode found for value " + mode));
        }
    }

    public static record Parameter(String className, int type, String typeName, ParameterMode mode, int precision,
            int scale, boolean signed, ParameterNullable nullable) {
    }
}