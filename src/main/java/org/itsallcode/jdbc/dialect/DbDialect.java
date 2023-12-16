package org.itsallcode.jdbc.dialect;

import org.itsallcode.jdbc.resultset.generic.SimpleMetaData.ColumnMetaData;

public interface DbDialect {
    Extractor createConverter(final ColumnMetaData column);
}
