package org.itsallcode.jdbc.statement;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.itsallcode.jdbc.dialect.ColumnValueSetter;
import org.itsallcode.jdbc.dialect.DbDialect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParamSetterProviderTest {

    @Mock
    DbDialect dialectMock;
    @Mock
    PreparedStatement stmtMock;
    @Mock
    ColumnValueSetter<Object> setterMock;

    @Test
    void findSetterForNull() throws SQLException {
        final ColumnValueSetter<Object> setter = testee().findSetter(null);
        setter.setObject(stmtMock, 0, null);
        verify(stmtMock).setObject(0, null);
    }

    @Test
    void findSetter() throws SQLException {
        final Object o = new Object();
        when(dialectMock.createSetter(Object.class)).thenReturn(setterMock);
        final ColumnValueSetter<Object> setter = testee().findSetter(o);
        assertSame(setterMock, setter);
    }

    @Test
    void cachesSetterSetter() throws SQLException {
        final Object o = new Object();
        when(dialectMock.createSetter(Object.class)).thenReturn(setterMock);
        final ParamSetterProvider testee = testee();
        testee.findSetter(o);
        testee.findSetter(o);
        verify(dialectMock, times(1)).createSetter(Object.class);
    }

    ParamSetterProvider testee() {
        return new ParamSetterProvider(dialectMock);
    }
}
