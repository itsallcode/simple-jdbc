package org.itsallcode.jdbc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.lang.reflect.*;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.itsallcode.jdbc.batch.BatchInsertRowBuilder;
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

    private BatchInsertRowBuilder<NameRow> testee() {
        final PreparedStatement stmt = createNoopPreparedStatement();
        when(connectionMock.prepareStatement(anyString()))
                .thenReturn(new SimplePreparedStatement(null, null, stmt, "sql"));
        return new BatchInsertRowBuilder<NameRow>(connectionMock::prepareStatement);
    }

    private PreparedStatement createNoopPreparedStatement() {
        final InvocationHandler invocationHandler = (final Object proxy, final Method method,
                final Object[] args) -> {
            switch (method.getName()) {
            case "executeBatch":
                return new int[0];

            default:
                return null;
            }
        };
        return (PreparedStatement) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class<?>[] { PreparedStatement.class }, invocationHandler);
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
