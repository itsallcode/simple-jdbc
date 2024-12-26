package org.itsallcode.jdbc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GenericParameterSetterTest {
    @Mock
    PreparedStatement preparedStatementMock;

    @Test
    void nullParameter() {
        assertThatThrownBy(() -> new GenericParameterSetter(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("parameters");
    }

    @Test
    void emptyParameterList() throws SQLException {
        setParameters(List.of());
        verifyNoInteractions(preparedStatementMock);
    }

    @Test
    void singleParameter() throws SQLException {
        setParameters(List.of(1));
        verify(preparedStatementMock).setObject(1, 1);
    }

    @Test
    void multipleParameters() throws SQLException {
        setParameters(List.of(1, "b", 3.14));
        final InOrder inOrder = inOrder(preparedStatementMock);
        inOrder.verify(preparedStatementMock).setObject(1, 1);
        inOrder.verify(preparedStatementMock).setObject(2, "b");
        inOrder.verify(preparedStatementMock).setObject(3, 3.14);
        inOrder.verifyNoMoreInteractions();
    }

    void setParameters(final List<Object> parameters) throws SQLException {
        new GenericParameterSetter(parameters).setValues(preparedStatementMock);
    }
}
