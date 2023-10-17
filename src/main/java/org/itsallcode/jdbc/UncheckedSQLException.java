package org.itsallcode.jdbc;

import java.sql.SQLException;

/**
 * This unchecked exception is thrown whenever a checked {@link SQLException} is
 * thrown.
 */
public class UncheckedSQLException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Create a new instance.
     * 
     * @param cause the cause
     */
    public UncheckedSQLException(final SQLException cause) {
        super(cause);
    }

    /**
     * Create a new instance.
     * 
     * @param message error message
     * @param cause   cause
     */
    public UncheckedSQLException(final String message, final SQLException cause) {
        super(message, cause);
    }
}
