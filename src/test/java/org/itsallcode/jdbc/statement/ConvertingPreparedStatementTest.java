package org.itsallcode.jdbc.statement;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.itsallcode.jdbc.dialect.ColumnValueSetter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConvertingPreparedStatementTest {
    @Mock
    PreparedStatement delegateMock;
    @Mock
    ParamSetterProvider paramSetterProviderMock;
    @Mock
    ColumnValueSetter<Object> paramSetterMock;

    @SuppressWarnings("resource")
    @Test
    void setObject() throws SQLException {
        final Object o = new Object();
        when(paramSetterProviderMock.findSetter(same(o))).thenReturn(paramSetterMock);
        new ConvertingPreparedStatement(delegateMock, paramSetterProviderMock).setObject(1, o);
        verify(paramSetterMock).setObject(same(delegateMock), eq(1), same(o));
    }
}
