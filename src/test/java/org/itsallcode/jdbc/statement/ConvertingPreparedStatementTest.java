package org.itsallcode.jdbc.statement;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.itsallcode.jdbc.ParameterMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConvertingPreparedStatementTest {
    @Mock
    PreparedStatement delegateMock;
    @Mock
    ParameterMapper parameterMapperMock;

    @SuppressWarnings("resource")
    @Test
    void setObject() throws SQLException {
        final Object o1 = new Object();
        final Object o2 = new Object();
        when(parameterMapperMock.map(same(o1))).thenReturn(o2);
        new ConvertingPreparedStatement(delegateMock, parameterMapperMock).setObject(1, o1);
        verify(delegateMock).setObject(eq(1), same(o2));
    }
}
