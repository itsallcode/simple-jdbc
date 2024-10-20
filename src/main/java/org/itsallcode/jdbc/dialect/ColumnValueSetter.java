package org.itsallcode.jdbc.dialect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ColumnValueSetter<T> {

    void setObject(PreparedStatement stmt, int parameterIndex, T object) throws SQLException;
}
