package org.itsallcode.jdbc.resultset.generic;

interface ValueConverter {
    <T> T convert(Object value, Class<T> type);
}
