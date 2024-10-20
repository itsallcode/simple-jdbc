package org.itsallcode.jdbc.statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DelegatingPreparedStatementTest extends DelegatingStatementTest {
    @Mock
    PreparedStatement preparedStatementMock;

    @Test
    void executeQueryWithoutArg() throws SQLException {
        testee().executeQuery();
        verify(getStatementMock()).executeQuery();
    }

    @Test
    void executeUpdateWithoutArg() throws SQLException {
        when(getStatementMock().executeUpdate()).thenReturn(1);
        assertEquals(1, testee().executeUpdate());
    }

    @Test
    void setNull() throws SQLException {
        testee().setNull(1, 2);
        verify(getStatementMock()).setNull(1, 2);
    }

    @Test
    void setBoolean() throws SQLException {
        testee().setBoolean(1, true);
        verify(getStatementMock()).setBoolean(1, true);
    }

    @Test
    void setByte() throws SQLException {
        testee().setByte(1, (byte) 2);
        verify(getStatementMock()).setByte(1, (byte) 2);
    }

    @Test
    void setShort() throws SQLException {
        testee().setShort(1, (short) 2);
        verify(getStatementMock()).setShort(1, (short) 2);
    }

    @Test
    void setInt() throws SQLException {
        testee().setInt(1, 2);
        verify(getStatementMock()).setInt(1, 2);
    }

    @Test
    void setLong() throws SQLException {
        testee().setLong(1, 2);
        verify(getStatementMock()).setLong(1, 2);
    }

    @Test
    void setFloat() throws SQLException {
        testee().setFloat(1, (float) 2.2);
        verify(getStatementMock()).setFloat(1, (float) 2.2);
    }

    @Test
    void setDouble() throws SQLException {
        testee().setDouble(1, 2.2);
        verify(getStatementMock()).setDouble(1, 2.2);
    }

    @Test
    void setBigDecimal() throws SQLException {
        testee().setBigDecimal(1, new BigDecimal(2));
        verify(getStatementMock()).setBigDecimal(1, new BigDecimal(2));
    }

    @Test
    void setString() throws SQLException {
        testee().setString(1, "x");
        verify(getStatementMock()).setString(1, "x");
    }

    @Test
    void setBytes() throws SQLException {
        testee().setBytes(1, new byte[] { (byte) 2 });
        verify(getStatementMock()).setBytes(1, new byte[] { (byte) 2 });
    }

    @Test
    void setDate(@Mock final Date date) throws SQLException {
        testee().setDate(1, date);
        verify(getStatementMock()).setDate(eq(1), same(date));
    }

    @Test
    void setTime(@Mock final Time time) throws SQLException {
        testee().setTime(1, time);
        verify(getStatementMock()).setTime(eq(1), same(time));
    }

    @Test
    void setTimestamp(@Mock final Timestamp timestamp) throws SQLException {
        testee().setTimestamp(1, timestamp);
        verify(getStatementMock()).setTimestamp(eq(1), same(timestamp));
    }

    @Test
    void setAsciiStream(@Mock final InputStream stream) throws SQLException {
        testee().setAsciiStream(1, stream);
        verify(getStatementMock()).setAsciiStream(eq(1), same(stream));
    }

    @Test
    @SuppressWarnings("deprecation")
    void setUnicodeStream(@Mock final InputStream stream) throws SQLException {
        testee().setUnicodeStream(1, stream, 2);
        verify(getStatementMock()).setUnicodeStream(eq(1), same(stream), eq(2));
    }

    @Test
    void setBinaryStream(@Mock final InputStream stream) throws SQLException {
        testee().setBinaryStream(1, stream);
        verify(getStatementMock()).setBinaryStream(eq(1), same(stream));
    }

    @Test
    void clearParameters() throws SQLException {
        testee().clearParameters();
        verify(getStatementMock()).clearParameters();
    }

    @Test
    void setObject(@Mock final Object object) throws SQLException {
        testee().setObject(1, object);
        verify(getStatementMock()).setObject(eq(1), same(object));
    }

    @Test
    void setObjectWithTargetType(@Mock final Object object) throws SQLException {
        testee().setObject(1, object, 2);
        verify(getStatementMock()).setObject(eq(1), same(object), eq(2));
    }

    @Test
    void executeWithoutArgs() throws SQLException {
        when(getStatementMock().execute()).thenReturn(true);
        assertTrue(testee().execute());
    }

    @Test
    void addBatchWithoutArgs() throws SQLException {
        testee().addBatch();
        verify(getStatementMock()).addBatch();
    }

    @Test
    void setCharacterStreamWithIntLength(@Mock final Reader x) throws SQLException {
        testee().setCharacterStream(1, x, 2);
        verify(getStatementMock()).setCharacterStream(eq(1), same(x), eq(2));
    }

    @Test
    void setRef(@Mock final Ref x) throws SQLException {
        testee().setRef(1, x);
        verify(getStatementMock()).setRef(eq(1), same(x));
    }

    @Test
    void setBlob(@Mock final Blob x) throws SQLException {
        testee().setBlob(1, x);
        verify(getStatementMock()).setBlob(eq(1), same(x));
    }

    @Test
    void setClob(@Mock final Clob x) throws SQLException {
        testee().setClob(1, x);
        verify(getStatementMock()).setClob(eq(1), same(x));
    }

    @Test
    void setArray(@Mock final Array x) throws SQLException {
        testee().setArray(1, x);
        verify(getStatementMock()).setArray(eq(1), same(x));
    }

    @Test
    void getMetaData(@Mock final ResultSetMetaData metaData) throws SQLException {
        when(getStatementMock().getMetaData()).thenReturn(metaData);
        assertSame(metaData, testee().getMetaData());
    }

    @Test
    void setDate(@Mock final Date x, @Mock final Calendar cal) throws SQLException {
        testee().setDate(1, x, cal);
        verify(getStatementMock()).setDate(eq(1), same(x), same(cal));
    }

    @Test
    void setTime(@Mock final Time x, @Mock final Calendar cal) throws SQLException {
        testee().setTime(1, x, cal);
        verify(getStatementMock()).setTime(eq(1), same(x), same(cal));
    }

    @Test
    void setTimestamp(@Mock final Timestamp x, @Mock final Calendar cal) throws SQLException {
        testee().setTimestamp(1, x, cal);
        verify(getStatementMock()).setTimestamp(eq(1), same(x), same(cal));
    }

    @Test
    void setNullWithType() throws SQLException {
        testee().setNull(1, 2, "type");
        verify(getStatementMock()).setNull(1, 2, "type");
    }

    @Test
    void setURL(@Mock final URL x) throws SQLException {
        testee().setURL(1, x);
        verify(getStatementMock()).setURL(eq(1), same(x));
    }

    @Test
    void getParameterMetaData(@Mock final ParameterMetaData metaData) throws SQLException {
        when(getStatementMock().getParameterMetaData()).thenReturn(metaData);
        assertSame(metaData, testee().getParameterMetaData());
    }

    @Test
    void setRowId(@Mock final RowId x) throws SQLException {
        testee().setRowId(1, x);
        verify(getStatementMock()).setRowId(eq(1), same(x));
    }

    @Test
    void setNString() throws SQLException {
        testee().setNString(1, "value");
        verify(getStatementMock()).setNString(1, "value");
    }

    @Test
    void setNCharacterStreamWithLength(@Mock final Reader value)
            throws SQLException {
        testee().setNCharacterStream(1, value, 2L);
        verify(getStatementMock()).setNCharacterStream(eq(1), same(value), eq(2L));
    }

    @Test
    void setNClob(@Mock final NClob value) throws SQLException {
        testee().setNClob(1, value);
        verify(getStatementMock()).setNClob(eq(1), same(value));
    }

    @Test
    void setClobWithLength(@Mock final Reader reader) throws SQLException {
        testee().setClob(1, reader, 2L);
        verify(getStatementMock()).setClob(eq(1), same(reader), eq(2L));
    }

    @Test
    void setBlobWithLength(@Mock final InputStream inputStream) throws SQLException {
        testee().setBlob(1, inputStream, 2L);
        verify(getStatementMock()).setBlob(eq(1), same(inputStream), eq(2L));
    }

    @Test
    void setNClobWithLength(@Mock final Reader reader) throws SQLException {
        testee().setNClob(1, reader, 2L);
        verify(getStatementMock()).setNClob(eq(1), same(reader), eq(2L));
    }

    @Test
    void setSQLXML(@Mock final SQLXML xmlObject) throws SQLException {
        testee().setSQLXML(1, xmlObject);
        verify(getStatementMock()).setSQLXML(eq(1), same(xmlObject));
    }

    @Test
    void setObjectWithTypeAndLength(@Mock final Object x) throws SQLException {
        testee().setObject(1, x, 2, 3);
        verify(getStatementMock()).setObject(eq(1), same(x), eq(2), eq(3));
    }

    @Test
    void setAsciiStreamWithIntLength(@Mock final InputStream x) throws SQLException {
        testee().setAsciiStream(1, x, 2);
        verify(getStatementMock()).setAsciiStream(eq(1), same(x), eq(2));
    }

    @Test
    void setBinaryStreamWithIntLength(@Mock final InputStream x) throws SQLException {
        testee().setBinaryStream(1, x, 2);
        verify(getStatementMock()).setBinaryStream(eq(1), same(x), eq(2));
    }

    @Test
    void setCharacterStreamWithLongLength(@Mock final Reader reader) throws SQLException {
        testee().setCharacterStream(1, reader, 2L);
        verify(getStatementMock()).setCharacterStream(eq(1), same(reader), eq(2L));
    }

    @Test
    void setAsciiStreamWithLongLength(@Mock final InputStream x) throws SQLException {
        testee().setAsciiStream(1, x, 2L);
        verify(getStatementMock()).setAsciiStream(eq(1), same(x), eq(2L));
    }

    @Test
    void setBinaryStreamWithLongLength(@Mock final InputStream x) throws SQLException {
        testee().setBinaryStream(1, x, 2L);
        verify(getStatementMock()).setBinaryStream(eq(1), same(x), eq(2L));
    }

    @Test
    void setCharacterStream(@Mock final Reader reader) throws SQLException {
        testee().setCharacterStream(1, reader);
        verify(getStatementMock()).setCharacterStream(eq(1), same(reader));
    }

    @Test
    void setNCharacterStream(@Mock final Reader value) throws SQLException {
        testee().setNCharacterStream(1, value);
        verify(getStatementMock()).setNCharacterStream(eq(1), same(value));
    }

    @Test
    void setClob(@Mock final Reader reader) throws SQLException {
        testee().setClob(1, reader);
        verify(getStatementMock()).setClob(eq(1), same(reader));
    }

    @Test
    void setBlob(@Mock final InputStream inputStream) throws SQLException {
        testee().setBlob(1, inputStream);
        verify(getStatementMock()).setBlob(eq(1), same(inputStream));
    }

    @Test
    void setNClob(@Mock final Reader reader) throws SQLException {
        testee().setNClob(1, reader);
        verify(getStatementMock()).setNClob(eq(1), same(reader));
    }

    @Override
    protected PreparedStatement getStatementMock() {
        return preparedStatementMock;
    }

    @Override
    protected DelegatingPreparedStatement testee() {
        return new DelegatingPreparedStatement(getStatementMock());
    }
}
