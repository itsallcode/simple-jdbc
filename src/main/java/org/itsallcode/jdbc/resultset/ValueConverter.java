package org.itsallcode.jdbc.resultset;

interface ValueConverter {
    <T> T convert(Object value, Class<T> type);
}
