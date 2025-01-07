package org.itsallcode.jdbc;

import java.lang.reflect.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.itsallcode.jdbc.batch.*;
import org.itsallcode.jdbc.dialect.GenericDialect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BatchInsertPerformanceTest {
    private static final int ROW_COUNT = 10_000_000;
    private static final int MAX_BATCH_SIZE = PreparedStatementBatchBuilder.DEFAULT_MAX_BATCH_SIZE;
    @Mock
    SimpleConnection connectionMock;
    @Mock
    SimplePreparedStatement preparedStatementMock;

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void rowStatementSetter() {
        rowTestee().into("TEST", List.of("ID", "NAME"))
                .mapping((row, stmt) -> {
                    stmt.setInt(1, row.id);
                    stmt.setString(2, row.name);
                }).rows(generateStream(ROW_COUNT)).start();
    }

    private RowPreparedStatementBatchBuilder<NameRow> rowTestee() {
        return new RowPreparedStatementBatchBuilder<NameRow>(this::prepareStatement).maxBatchSize(MAX_BATCH_SIZE);
    }

    private PreparedStatementBatchBuilder testee() {
        return new PreparedStatementBatchBuilder(this::prepareStatement).maxBatchSize(MAX_BATCH_SIZE);
    }

    private SimplePreparedStatement prepareStatement(final String sql) {
        final PreparedStatement stmt = createNoopPreparedStatement();
        return new SimplePreparedStatement(Context.builder().build(), GenericDialect.INSTANCE, stmt, sql);
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
    void objectArray() {
        rowTestee().into("TEST", List.of("ID", "NAME"))
                .mapping(row -> new Object[] { row.id, row.name })
                .rows(generateStream(ROW_COUNT))
                .start();
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void directAdd() {
        try (PreparedStatementBatch batch = testee().into("TEST", List.of("ID", "NAME")).build()) {
            for (int i = 0; i < ROW_COUNT; i++) {
                final int row = i;
                batch.add(ps -> {
                    ps.setInt(1, row);
                    ps.setString(2, "name" + row);
                });
            }
        }
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void directAddBatch() throws SQLException {
        try (PreparedStatementBatch batch = testee().into("TEST", List.of("ID", "NAME")).build()) {
            final PreparedStatement statement = batch.getStatement();
            for (int i = 0; i < ROW_COUNT; i++) {
                statement.setInt(1, i);
                statement.setString(2, "Name " + i);
                batch.addBatch();
            }
        }
    }

    private Stream<NameRow> generateStream(final int rowCount) {
        return IntStream.range(0, rowCount).mapToObj(id -> new NameRow(id, "Name " + id));
    }

    private record NameRow(int id, String name) {

    }
}
