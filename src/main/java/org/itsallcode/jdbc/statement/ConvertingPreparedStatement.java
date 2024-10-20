package org.itsallcode.jdbc.statement;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * A {@link PreparedStatement} that delegates calls to
 * {@link #setObject(int, Object)} to a setter provided by
 * {@link ParamSetterProvider#findSetter(Object)}.
 */
public class ConvertingPreparedStatement extends DelegatingPreparedStatement {
    private final PreparedStatement originalDelegate;
    private final ParamSetterProvider paramSetterProvider;

    /**
     * Create a new instance.
     * 
     * @param delegate            delegate
     * @param paramSetterProvider parameter setter provider
     */
    public ConvertingPreparedStatement(final PreparedStatement delegate,
            final ParamSetterProvider paramSetterProvider) {
        super(delegate);
        this.originalDelegate = delegate;
        this.paramSetterProvider = paramSetterProvider;
    }

    @Override
    public void setObject(final int parameterIndex, final Object x) throws SQLException {
        paramSetterProvider.findSetter(x)
                .setObject(originalDelegate, parameterIndex, x);
    }
}
