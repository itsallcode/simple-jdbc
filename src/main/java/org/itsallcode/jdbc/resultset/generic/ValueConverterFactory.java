package org.itsallcode.jdbc.resultset.generic;

class ValueConverterFactory {
    ValueConverter build(final ColumnType type) {
        return new Converter(type);
    }

    private static class Converter implements ValueConverter {

        private final ColumnType type;

        private Converter(final ColumnType type) {
            this.type = type;
        }

        @Override
        public <T> T convert(final Object value, final Class<T> type) {
            if (type.isInstance(value)) {
                return type.cast(value);
            }

            throw new IllegalStateException("Can't convert value '" + value + "' of type " + value.getClass().getName()
                    + " to " + type.getName());
        }
    }
}
