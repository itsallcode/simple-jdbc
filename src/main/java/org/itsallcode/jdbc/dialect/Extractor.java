package org.itsallcode.jdbc.dialect;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface Extractor {

    Object getObject(ResultSet resultSet, int columnIndex) throws SQLException;

}