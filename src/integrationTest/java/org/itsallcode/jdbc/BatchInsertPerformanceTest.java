package org.itsallcode.jdbc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BatchInsertPerformanceTest {
    @Mock
    SimpleConnection connectionMock;
    @Mock
    SimplePreparedStatement preparedStatementMock;

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void performanceTestRowStmtSetter() {
        final int rowCount = 10_000_000;
        testee().into("TEST", List.of("ID", "NAME"))
                .mapping((row, stmt) -> {
                    stmt.setInt(1, row.id);
                    stmt.setString(2, row.name);
                }).rows(generateStream(rowCount)).start();
    }

    private BatchInsertBuilder<NameRow> testee() {
        final PreparedStatement stmt = new NoopPreparedStatement();
        when(connectionMock.prepareStatement(anyString()))
                .thenReturn(new SimplePreparedStatement(null, null, stmt, "sql"));
        return new BatchInsertBuilder<NameRow>(connectionMock::prepareStatement, Context.builder().build());
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void performanceTestObjectArray() {
        final int rowCount = 10_000_000;
        testee().into("TEST", List.of("ID", "NAME"))
                .mapping(row -> new Object[] { row.id, row.name }).rows(generateStream(rowCount)).start();
    }

    private Stream<NameRow> generateStream(final int rowCount) {
        return IntStream.range(0, rowCount).mapToObj(id -> new NameRow(id, "Name " + id));
    }

    private record NameRow(int id, String name) {

    }
}
