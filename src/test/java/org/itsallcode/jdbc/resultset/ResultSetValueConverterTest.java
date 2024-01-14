package org.itsallcode.jdbc.resultset;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.itsallcode.jdbc.resultset.generic.ColumnMetaData;
import org.itsallcode.jdbc.resultset.generic.SimpleMetaData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResultSetValueConverterTest {

    @Mock
    ResultSet resultSetMock;

    @Test
    void emptyInputNoConverterFoundForIndex() throws SQLException {
        final ResultSetValueConverter converter = testee(emptyList(), emptyList());
        assertThatThrownBy(() -> converter.getObject(resultSetMock, 1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No converter found for column index 1");
    }

    @Test
    void emptyInputNoConverterFoundForLabel() throws SQLException {
        final ResultSetValueConverter converter = testee(emptyList(), emptyList());
        assertThatThrownBy(() -> converter.getObject(resultSetMock, "label"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No index found for column label 'label'. Available column labels: []");
    }

    ResultSetValueConverter testee(final List<ColumnMetaData> columns, final List<ColumnValueConverter> converters) {
        return ResultSetValueConverter.create(new SimpleMetaData(columns), converters);
    }
}
