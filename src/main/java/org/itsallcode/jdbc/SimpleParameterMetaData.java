package org.itsallcode.jdbc;

import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Wrapper for {@link ParameterMetaData} that simplifies usage.
 */
public class SimpleParameterMetaData {

    private final ParameterMetaData metaData;

    SimpleParameterMetaData(final ParameterMetaData parameterMetaData) {
        this.metaData = parameterMetaData;
    }

    /**
     * Get all parameters.
     * 
     * @return all parameters
     */
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

    /**
     * Parameter type.
     */
    public enum ParameterMode {
        /** Parameter mode IN */
        IN(ParameterMetaData.parameterModeIn),
        /** Parameter mode INOUT */
        INOUT(ParameterMetaData.parameterModeInOut),
        /** Parameter mode OUT */
        OUT(ParameterMetaData.parameterModeOut),
        /** Parameter mode is unknown */
        UNKNWON(ParameterMetaData.parameterModeUnknown);

        private final int mode;

        private ParameterMode(final int mode) {
            this.mode = mode;
        }

        private static ParameterMode of(final int mode) {
            return Arrays.stream(values())
                    .filter(m -> m.mode == mode)
                    .findAny()
                    .orElseThrow(
                            () -> new IllegalArgumentException("No parameter mode found for value " + mode));
        }
    }

    /**
     * Parameter nullability status.
     */
    public enum ParameterNullable {
        /** Parameter will not allow {@code NULL} values. */
        NO_NULLS(ParameterMetaData.parameterNoNulls),
        /** Parameter will allow {@code NULL} values. */
        NULLABLE(ParameterMetaData.parameterNullable),
        /** Parameter nullability status is unknown. */
        UNKNWON(ParameterMetaData.parameterNullableUnknown);

        private final int mode;

        private ParameterNullable(final int mode) {
            this.mode = mode;
        }

        private static ParameterNullable of(final int mode) {
            return Arrays.stream(values()).filter(m -> m.mode == mode).findAny().orElseThrow(
                    () -> new IllegalArgumentException("No parameter mode found for value " + mode));
        }
    }

    /**
     * A parameter for a prepared statement.
     * 
     * @param className class name of the parameter type
     * @param type      JDBC type of the parameter
     * @param typeName  name of the parameter type
     * @param mode      parameter mode
     * @param precision parameter precision
     * @param scale     parameter scale
     * @param signed    {@code true} if the parameter is signed
     * @param nullable  nullability of the parameter
     */
    public static record Parameter(String className, int type, String typeName, ParameterMode mode, int precision,
            int scale, boolean signed, ParameterNullable nullable) {
    }
}