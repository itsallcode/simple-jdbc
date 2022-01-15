package org.itsallcode.jdbc;

import java.sql.SQLException;

public class UncheckedSQLException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public UncheckedSQLException(SQLException cause)
    {
        super(cause);
    }

    public UncheckedSQLException(String message, SQLException cause)
    {
        super(message, cause);
    }
}
