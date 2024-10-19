package org.itsallcode.jdbc.statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DelegatingStatementTest {
    @Mock
    Statement statementMock;

    @Test
    void unwrap() throws SQLException {
        when(statementMock.unwrap(String.class)).thenReturn("value");
        assertEquals("value", testee().unwrap(String.class));
    }

    @Test
    void isWrapperFor() throws SQLException {
        when(statementMock.isWrapperFor(String.class)).thenReturn(true);
        assertTrue(testee().isWrapperFor(String.class));
    }

    DelegatingStatement testee() {
        return new DelegatingStatement(statementMock);
    }
}
