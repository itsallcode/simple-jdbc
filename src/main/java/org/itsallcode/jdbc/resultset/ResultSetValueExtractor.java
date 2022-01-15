package org.itsallcode.jdbc.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetValueExtractor
{
    ResultSetValue extractValue(ResultSet resultSet, int columnIndex) throws SQLException;
}
