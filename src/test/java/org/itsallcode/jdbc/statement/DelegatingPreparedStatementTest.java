package org.itsallcode.jdbc.statement;

import java.sql.PreparedStatement;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DelegatingPreparedStatementTest extends DelegatingStatementTest {
    @Mock
    PreparedStatement preparedStatementMock;

    @Override
    protected PreparedStatement getStatementMock() {
        return preparedStatementMock;
    }

    @Override
    protected DelegatingStatement testee() {
        return new DelegatingPreparedStatement(getStatementMock());
    }
}
