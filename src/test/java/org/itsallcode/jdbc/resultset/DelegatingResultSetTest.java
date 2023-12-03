package org.itsallcode.jdbc.resultset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DelegatingResultSetTest {

    private static final String COL_LABEL = "c";
    private static final int COL_INDEX = 1;

    @Mock
    ResultSet resultSetMock;

    @Test
    void unwrap() throws SQLException {
        when(resultSetMock.unwrap(String.class)).thenReturn("value");
        assertEquals("value", testee().unwrap(String.class));
    }

    @Test
    void isWrapperFor() throws SQLException {
        when(resultSetMock.isWrapperFor(String.class)).thenReturn(true);
        assertEquals(true, testee().isWrapperFor(String.class));
    }

    @Test
    void next() throws SQLException {
        when(resultSetMock.next()).thenReturn(true);
        assertEquals(true, testee().next());
    }

    @Test
    void close() throws SQLException {
        testee().close();
        verify(resultSetMock).close();
    }

    @Test
    void wasNull() throws SQLException {
        when(resultSetMock.wasNull()).thenReturn(true);
        assertEquals(true, testee().wasNull());
    }

    @Test
    public String getString(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getString'");
    }

    @Test
    void getBoolean(final int columnIndex) throws SQLException {
        when(resultSetMock.getBoolean(1)).thenReturn(true);
        assertEquals(true, testee().getBoolean(1));
    }

    @Test
    void getByte(final int columnIndex) throws SQLException {
        when(resultSetMock.getByte(COL_INDEX)).thenReturn((byte) 2);
        assertEquals((byte) 2, testee().getByte(COL_INDEX));
    }

    @Test
    void getShort(final int columnIndex) throws SQLException {
        when(resultSetMock.getShort(COL_INDEX)).thenReturn((short) 2);
        assertEquals((short) 2, testee().getShort(COL_INDEX));
    }

    @Test
    void getInt(final int columnIndex) throws SQLException {
        when(resultSetMock.getInt(COL_INDEX)).thenReturn(2);
        assertEquals(2, testee().getInt(COL_INDEX));
    }

    @Test
    void getLong(final int columnIndex) throws SQLException {
        when(resultSetMock.getLong(COL_INDEX)).thenReturn(2L);
        assertEquals(2L, testee().getLong(COL_INDEX));
    }

    @Test
    void getFloat(final int columnIndex) throws SQLException {
        when(resultSetMock.getFloat(COL_INDEX)).thenReturn(2.2F);
        assertEquals(2, testee().getFloat(COL_INDEX));
    }

    @Test
    public double getDouble(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDouble'");
    }

    @Test
    @Deprecated
    public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBigDecimal'");
    }

    @Test
    void getBytes(final int columnIndex) throws SQLException {
        when(resultSetMock.getBytes(COL_INDEX)).thenReturn(new byte[] { 2 });
        assertEquals(new byte[] { 2 }, testee().getBytes(COL_INDEX));
    }

    @Test
    public Date getDate(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDate'");
    }

    @Test
    public Time getTime(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTime'");
    }

    @Test
    public Timestamp getTimestamp(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTimestamp'");
    }

    @Test
    public InputStream getAsciiStream(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAsciiStream'");
    }

    @Test
    @Deprecated
    public InputStream getUnicodeStream(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUnicodeStream'");
    }

    @Test
    public InputStream getBinaryStream(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBinaryStream'");
    }

    @Test
    public String getString(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getString'");
    }

    @Test
    void getBoolean() throws SQLException {
        when(resultSetMock.getBoolean("a")).thenReturn(true);
        assertEquals(true, testee().getBoolean("a"));
    }

    @Test
    void getByte(final String columnLabel) throws SQLException {
        when(resultSetMock.getByte(COL_LABEL)).thenReturn((byte) 2);
        assertEquals((byte) 2, testee().getByte(COL_LABEL));
    }

    @Test
    void getShort(final String columnLabel) throws SQLException {
        when(resultSetMock.getShort(COL_LABEL)).thenReturn((short) 2);
        assertEquals((short) 2, testee().getShort(COL_LABEL));
    }

    @Test
    void getInt(final String columnLabel) throws SQLException {
        when(resultSetMock.getInt(COL_LABEL)).thenReturn(2);
        assertEquals(2, testee().getInt(COL_LABEL));
    }

    @Test
    void getLong(final String columnLabel) throws SQLException {
        when(resultSetMock.getLong(COL_LABEL)).thenReturn(2L);
        assertEquals(2L, testee().getLong(COL_LABEL));
    }

    @Test
    void getFloat(final String columnLabel) throws SQLException {
        when(resultSetMock.getFloat(COL_LABEL)).thenReturn(2.2F);
        assertEquals(2, testee().getFloat(COL_LABEL));
    }

    @Test
    public double getDouble(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDouble'");
    }

    @Test
    @Deprecated
    public BigDecimal getBigDecimal(final String columnLabel, final int scale) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBigDecimal'");
    }

    @Test
    void getBytes(final String columnLabel) throws SQLException {
        when(resultSetMock.getBytes(COL_LABEL)).thenReturn(new byte[] { 2 });
        assertEquals(new byte[] { 2 }, testee().getBytes(COL_LABEL));
    }

    @Test
    public Date getDate(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDate'");
    }

    @Test
    public Time getTime(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTime'");
    }

    @Test
    public Timestamp getTimestamp(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTimestamp'");
    }

    @Test
    public InputStream getAsciiStream(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAsciiStream'");
    }

    @Test
    @Deprecated
    public InputStream getUnicodeStream(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUnicodeStream'");
    }

    @Test
    public InputStream getBinaryStream(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBinaryStream'");
    }

    @Test
    public SQLWarning getWarnings() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWarnings'");
    }

    @Test
    void clearWarnings() throws SQLException {
        testee().clearWarnings();
        verify(resultSetMock).clearWarnings();
    }

    @Test
    public String getCursorName() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCursorName'");
    }

    @Test
    public ResultSetMetaData getMetaData() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMetaData'");
    }

    @Test
    public Object getObject(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getObject'");
    }

    @Test
    public Object getObject(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getObject'");
    }

    @Test
    void findColumn(final String columnLabel) throws SQLException {
        when(resultSetMock.findColumn(COL_LABEL)).thenReturn(2);
        assertEquals(2, testee().findColumn(COL_LABEL));
    }

    @Test
    public Reader getCharacterStream(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCharacterStream'");
    }

    @Test
    public Reader getCharacterStream(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCharacterStream'");
    }

    @Test
    public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBigDecimal'");
    }

    @Test
    public BigDecimal getBigDecimal(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBigDecimal'");
    }

    @Test
    void isBeforeFirst() throws SQLException {
        when(resultSetMock.isBeforeFirst()).thenReturn(true);
        assertEquals(true, testee().isBeforeFirst());
    }

    @Test
    void isAfterLast() throws SQLException {
        when(resultSetMock.isAfterLast()).thenReturn(true);
        assertEquals(true, testee().isAfterLast());
    }

    @Test
    void isFirst() throws SQLException {
        when(resultSetMock.isFirst()).thenReturn(true);
        assertEquals(true, testee().isFirst());
    }

    @Test
    void isLast() throws SQLException {
        when(resultSetMock.isLast()).thenReturn(true);
        assertEquals(true, testee().isLast());
    }

    @Test
    void beforeFirst() throws SQLException {
        testee().beforeFirst();
        verify(resultSetMock).beforeFirst();
    }

    @Test
    void afterLast() throws SQLException {
        testee().afterLast();
        verify(resultSetMock).afterLast();
    }

    @Test
    void first() throws SQLException {
        when(resultSetMock.first()).thenReturn(true);
        assertEquals(true, testee().first());
    }

    @Test
    void last() throws SQLException {
        when(resultSetMock.last()).thenReturn(true);
        assertEquals(true, testee().last());
    }

    @Test
    void getRow() throws SQLException {
        when(resultSetMock.getRow()).thenReturn(1);
        assertEquals(1, testee().getRow());
    }

    @Test
    void absolute(final int row) throws SQLException {
        when(resultSetMock.absolute(1)).thenReturn(true);
        assertEquals(true, testee().absolute(1));
    }

    @Test
    void relative(final int rows) throws SQLException {
        when(resultSetMock.relative(1)).thenReturn(true);
        assertEquals(true, testee().relative(1));
    }

    @Test
    void previous() throws SQLException {
        when(resultSetMock.previous()).thenReturn(true);
        assertEquals(true, testee().previous());
    }

    @Test
    void setFetchDirection(final int direction) throws SQLException {
        testee().setFetchDirection(1);
        verify(resultSetMock).setFetchDirection(1);
    }

    @Test
    void getFetchDirection() throws SQLException {
        when(resultSetMock.getFetchDirection()).thenReturn(1);
        assertEquals(1, testee().getFetchDirection());
    }

    @Test
    void setFetchSize(final int rows) throws SQLException {
        testee().setFetchSize(1);
        verify(resultSetMock).setFetchSize(1);
    }

    @Test
    void getFetchSize() throws SQLException {
        when(resultSetMock.getFetchSize()).thenReturn(1);
        assertEquals(1, testee().getFetchSize());
    }

    @Test
    void getType() throws SQLException {
        when(resultSetMock.getType()).thenReturn(1);
        assertEquals(1, testee().getType());
    }

    @Test
    void getConcurrency() throws SQLException {
        when(resultSetMock.getConcurrency()).thenReturn(1);
        assertEquals(1, testee().getConcurrency());
    }

    @Test
    void rowUpdated() throws SQLException {
        when(resultSetMock.rowUpdated()).thenReturn(true);
        assertEquals(true, testee().rowUpdated());
    }

    @Test
    void rowInserted() throws SQLException {
        when(resultSetMock.rowInserted()).thenReturn(true);
        assertEquals(true, testee().rowInserted());
    }

    @Test
    void rowDeleted() throws SQLException {
        when(resultSetMock.rowDeleted()).thenReturn(true);
        assertEquals(true, testee().rowDeleted());
    }

    @Test
    void updateNull(final int columnIndex) throws SQLException {
        testee().updateNull(1);
        verify(resultSetMock).updateNull(1);
    }

    @Test
    void updateBoolean(final int columnIndex, final boolean x) throws SQLException {
        testee().updateBoolean(COL_INDEX, true);
        verify(resultSetMock).updateBoolean(COL_INDEX, true);
    }

    @Test
    void updateByte(final int columnIndex, final byte x) throws SQLException {
        testee().updateByte(COL_INDEX, (byte) 2);
        verify(resultSetMock).updateByte(COL_INDEX, (byte) 2);
    }

    @Test
    void updateShort(final int columnIndex, final short x) throws SQLException {
        testee().updateShort(COL_INDEX, (short) 2);
        verify(resultSetMock).updateShort(COL_INDEX, (short) 2);
    }

    @Test
    void updateInt(final int columnIndex, final int x) throws SQLException {
        testee().updateInt(COL_INDEX, 2);
        verify(resultSetMock).updateInt(COL_INDEX, 2);
    }

    @Test
    void updateLong(final int columnIndex, final long x) throws SQLException {
        testee().updateLong(COL_INDEX, 2);
        verify(resultSetMock).updateLong(COL_INDEX, 2);
    }

    @Test
    void updateFloat(final int columnIndex, final float x) throws SQLException {
        testee().updateFloat(COL_INDEX, 2.2F);
        verify(resultSetMock).updateFloat(COL_INDEX, 2.2F);
    }

    @Test
    void updateDouble(final int columnIndex, final double x) throws SQLException {
        testee().updateDouble(COL_INDEX, 2.2);
        verify(resultSetMock).updateDouble(COL_INDEX, 2.2);
    }

    @Test
    void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException {
        testee().updateBigDecimal(COL_INDEX, BigDecimal.TEN);
        verify(resultSetMock).updateBigDecimal(COL_INDEX, BigDecimal.TEN);
    }

    @Test
    void updateString(final int columnIndex, final String x) throws SQLException {
        testee().updateString(COL_INDEX, "a");
        verify(resultSetMock).updateString(COL_INDEX, "a");
    }

    @Test
    void updateBytes(final int columnIndex, final byte[] x) throws SQLException {
        testee().updateBytes(COL_INDEX, new byte[] { 1 });
        verify(resultSetMock).updateBytes(COL_INDEX, new byte[] { 1 });
    }

    @Test
    void updateDate(final int columnIndex, final Date x) throws SQLException {
        testee().updateDate(COL_INDEX, new Date(2));
        verify(resultSetMock).updateDate(COL_INDEX, new Date(2));
    }

    @Test
    void updateTime(final int columnIndex, final Time x) throws SQLException {
        testee().updateTime(COL_INDEX, new Time(2));
        verify(resultSetMock).updateTime(COL_INDEX, new Time(2));
    }

    @Test
    void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException {
        testee().updateTimestamp(COL_INDEX, new Timestamp(2));
        verify(resultSetMock).updateTimestamp(COL_INDEX, new Timestamp(2));
    }

    @Test
    void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateAsciiStream(COL_INDEX, stream, 3);
        verify(resultSetMock).updateAsciiStream(eq(COL_INDEX), same(stream), eq(3));
    }

    @Test
    void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBinaryStream(COL_INDEX, stream, 3);
        verify(resultSetMock).updateBinaryStream(eq(COL_INDEX), same(stream), eq(3));
    }

    @Test
    void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateCharacterStream(COL_INDEX, reader, 3);
        verify(resultSetMock).updateCharacterStream(eq(COL_INDEX), same(reader), eq(3));
    }

    @Test
    void updateObject(final int columnIndex, final Object x, final int scaleOrLength) throws SQLException {
        final Object object = new Object();
        testee().updateObject(COL_INDEX, object, 3);
        verify(resultSetMock).updateObject(eq(COL_INDEX), same(object), eq(3));
    }

    @Test
    void updateObject(final int columnIndex, final Object x) throws SQLException {
        final Object object = new Object();
        testee().updateObject(COL_INDEX, object);
        verify(resultSetMock).updateObject(eq(COL_INDEX), same(object));
    }

    @Test
    void updateNull(final String columnLabel) throws SQLException {
        testee().updateNull("c");
        verify(resultSetMock).updateNull("c");
    }

    @Test
    void updateBoolean(final String columnLabel, final boolean x) throws SQLException {
        testee().updateBoolean("c", true);
        verify(resultSetMock).updateBoolean("c", true);
    }

    @Test
    void updateByte(final String columnLabel, final byte x) throws SQLException {
        testee().updateByte("c", (byte) 2);
        verify(resultSetMock).updateByte("c", (byte) 2);
    }

    @Test
    void updateShort(final String columnLabel, final short x) throws SQLException {
        testee().updateShort("c", (short) 2);
        verify(resultSetMock).updateShort("c", (short) 2);

    }

    @Test
    void updateInt(final String columnLabel, final int x) throws SQLException {
        testee().updateInt("c", 2);
        verify(resultSetMock).updateInt("c", 2);
    }

    @Test
    void updateLong(final String columnLabel, final long x) throws SQLException {
        testee().updateLong("c", 2);
        verify(resultSetMock).updateLong("c", 2);
    }

    @Test
    void updateFloat(final String columnLabel, final float x) throws SQLException {
        testee().updateFloat(COL_LABEL, 2.2F);
        verify(resultSetMock).updateFloat(COL_LABEL, 2.2F);
    }

    @Test
    void updateDouble(final String columnLabel, final double x) throws SQLException {
        testee().updateDouble(COL_LABEL, 2.2);
        verify(resultSetMock).updateDouble(COL_LABEL, 2.2);
    }

    @Test
    void updateBigDecimal(final String columnLabel, final BigDecimal x) throws SQLException {
        testee().updateBigDecimal(COL_LABEL, BigDecimal.TEN);
        verify(resultSetMock).updateBigDecimal(COL_LABEL, BigDecimal.TEN);
    }

    @Test
    void updateString(final String columnLabel, final String x) throws SQLException {
        testee().updateString(COL_LABEL, "a");
        verify(resultSetMock).updateString(COL_LABEL, "a");
    }

    @Test
    void updateBytes(final String columnLabel, final byte[] x) throws SQLException {
        testee().updateBytes(COL_LABEL, new byte[] { 1 });
        verify(resultSetMock).updateBytes(COL_LABEL, new byte[] { 1 });
    }

    @Test
    void updateDate(final String columnLabel, final Date x) throws SQLException {
        testee().updateDate(COL_LABEL, new Date(2));
        verify(resultSetMock).updateDate(COL_LABEL, new Date(2));
    }

    @Test
    void updateTime(final String columnLabel, final Time x) throws SQLException {
        testee().updateTime(COL_LABEL, new Time(2));
        verify(resultSetMock).updateTime(COL_LABEL, new Time(2));
    }

    @Test
    void updateTimestamp(final String columnLabel, final Timestamp x) throws SQLException {
        testee().updateTimestamp(COL_LABEL, new Timestamp(2));
        verify(resultSetMock).updateTimestamp(COL_LABEL, new Timestamp(2));
    }

    @Test
    void updateAsciiStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateAsciiStream(COL_LABEL, stream, 3);
        verify(resultSetMock).updateAsciiStream(eq(COL_LABEL), same(stream), eq(3));
    }

    @Test
    void updateBinaryStream(final String columnLabel, final InputStream x, final int length)
            throws SQLException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBinaryStream(COL_LABEL, stream, 3);
        verify(resultSetMock).updateBinaryStream(eq(COL_LABEL), same(stream), eq(3));
    }

    @Test
    void updateCharacterStream(final String columnLabel, final Reader r, final int length)
            throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateCharacterStream(COL_LABEL, reader, 3);
        verify(resultSetMock).updateCharacterStream(eq(COL_LABEL), same(reader), eq(3));
    }

    @Test
    void updateObject(final String columnLabel, final Object x, final int scaleOrLength) throws SQLException {
        final Object object = new Object();
        testee().updateObject(COL_LABEL, object, 3);
        verify(resultSetMock).updateObject(eq(COL_LABEL), same(object), eq(3));
    }

    @Test
    void updateObject(final String columnLabel, final Object x) throws SQLException {
        final Object object = new Object();
        testee().updateObject(COL_LABEL, object);
        verify(resultSetMock).updateObject(eq(COL_LABEL), same(object));
    }

    @Test
    void insertRow() throws SQLException {
        testee().insertRow();
        verify(resultSetMock).insertRow();
    }

    @Test
    void updateRow() throws SQLException {
        testee().updateRow();
        verify(resultSetMock).updateRow();
    }

    @Test
    void deleteRow() throws SQLException {
        testee().deleteRow();
        verify(resultSetMock).deleteRow();
    }

    @Test
    void refreshRow() throws SQLException {
        testee().refreshRow();
        verify(resultSetMock).refreshRow();
    }

    @Test
    void cancelRowUpdates() throws SQLException {
        testee().cancelRowUpdates();
        verify(resultSetMock).cancelRowUpdates();
    }

    @Test
    void moveToInsertRow() throws SQLException {
        testee().moveToInsertRow();
        verify(resultSetMock).moveToInsertRow();
    }

    @Test
    void moveToCurrentRow() throws SQLException {
        testee().moveToCurrentRow();
        verify(resultSetMock).moveToCurrentRow();
    }

    @Test
    public Statement getStatement() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStatement'");
    }

    @Test
    public Object getObject(final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getObject'");
    }

    @Test
    public Ref getRef(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRef'");
    }

    @Test
    public Blob getBlob(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBlob'");
    }

    @Test
    public Clob getClob(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getClob'");
    }

    @Test
    public Array getArray(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArray'");
    }

    @Test
    public Object getObject(final String columnLabel, final Map<String, Class<?>> map) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getObject'");
    }

    @Test
    public Ref getRef(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRef'");
    }

    @Test
    public Blob getBlob(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBlob'");
    }

    @Test
    public Clob getClob(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getClob'");
    }

    @Test
    public Array getArray(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArray'");
    }

    @Test
    public Date getDate(final int columnIndex, final Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDate'");
    }

    @Test
    public Date getDate(final String columnLabel, final Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDate'");
    }

    @Test
    public Time getTime(final int columnIndex, final Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTime'");
    }

    @Test
    public Time getTime(final String columnLabel, final Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTime'");
    }

    @Test
    public Timestamp getTimestamp(final int columnIndex, final Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTimestamp'");
    }

    @Test
    public Timestamp getTimestamp(final String columnLabel, final Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTimestamp'");
    }

    @Test
    public URL getURL(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getURL'");
    }

    @Test
    public URL getURL(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getURL'");
    }

    @Test
    void updateRef(final int columnIndex, final Ref x) throws SQLException {
        final Ref ref = mock(Ref.class);
        testee().updateRef(COL_INDEX, ref);
        verify(resultSetMock).updateRef(eq(COL_INDEX), same(ref));
    }

    @Test
    void updateRef(final String columnLabel, final Ref x) throws SQLException {
        final Ref ref = mock(Ref.class);
        testee().updateRef(COL_LABEL, ref);
        verify(resultSetMock).updateRef(eq(COL_LABEL), same(ref));
    }

    @Test
    void updateBlob(final int columnIndex, final Blob x) throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBlob(COL_INDEX, stream);
        verify(resultSetMock).updateBlob(eq(COL_INDEX), same(stream));
    }

    @Test
    void updateBlob(final String columnLabel, final Blob x) throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBlob(COL_LABEL, stream);
        verify(resultSetMock).updateBlob(eq(COL_LABEL), same(stream));
    }

    @Test
    void updateClob(final int columnIndex, final Clob x) throws SQLException {
        final Clob clob = mock(Clob.class);
        testee().updateClob(COL_INDEX, clob);
        verify(resultSetMock).updateClob(eq(COL_INDEX), same(clob));
    }

    @Test
    void updateClob(final String columnLabel, final Clob x) throws SQLException {
        final Clob clob = mock(Clob.class);
        testee().updateClob(COL_LABEL, clob);
        verify(resultSetMock).updateClob(eq(COL_LABEL), same(clob));
    }

    @Test
    void updateArray(final int columnIndex, final Array x) throws SQLException {
        final Array array = mock(Array.class);
        testee().updateArray(COL_INDEX, array);
        verify(resultSetMock).updateArray(eq(COL_INDEX), same(array));
    }

    @Test
    void updateArray(final String columnLabel, final Array x) throws SQLException {
        final Array array = mock(Array.class);
        testee().updateArray(COL_LABEL, array);
        verify(resultSetMock).updateArray(eq(COL_LABEL), same(array));
    }

    @Test
    public RowId getRowId(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRowId'");
    }

    @Test
    public RowId getRowId(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRowId'");
    }

    @Test
    void updateRowId(final int columnIndex, final RowId x) throws SQLException {
        final RowId rowId = mock(RowId.class);
        testee().updateRowId(COL_INDEX, rowId);
        verify(resultSetMock).updateRowId(eq(COL_INDEX), same(rowId));
    }

    @Test
    void updateRowId(final String columnLabel, final RowId x) throws SQLException {
        final RowId rowId = mock(RowId.class);
        testee().updateRowId(COL_LABEL, rowId);
        verify(resultSetMock).updateRowId(eq(COL_LABEL), same(rowId));
    }

    @Test
    void getHoldability() throws SQLException {
        when(resultSetMock.getHoldability()).thenReturn(1);
        assertEquals(1, testee().getHoldability());
    }

    @Test
    void isClosed() throws SQLException {
        when(resultSetMock.isClosed()).thenReturn(true);
        assertEquals(true, testee().isClosed());
    }

    @Test
    void updateNString(final int columnIndex, final String nString) throws SQLException {
        testee().updateNString(COL_INDEX, "a");
        verify(resultSetMock).updateNString(COL_INDEX, "a");
    }

    @Test
    void updateNString(final String columnLabel, final String nString) throws SQLException {
        testee().updateNString(COL_LABEL, "a");
        verify(resultSetMock).updateNString(COL_LABEL, "a");
    }

    @Test
    void updateNClob(final int columnIndex, final NClob nClob) throws SQLException {
        final NClob clob = mock(NClob.class);
        testee().updateNClob(COL_INDEX, clob);
        verify(resultSetMock).updateNClob(eq(COL_INDEX), same(clob));
    }

    @Test
    void updateNClob(final String columnLabel, final NClob nClob) throws SQLException {
        final NClob clob = mock(NClob.class);
        testee().updateNClob(COL_LABEL, clob);
        verify(resultSetMock).updateNClob(eq(COL_LABEL), same(clob));
    }

    @Test
    public NClob getNClob(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNClob'");
    }

    @Test
    public NClob getNClob(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNClob'");
    }

    @Test
    public SQLXML getSQLXML(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSQLXML'");
    }

    @Test
    public SQLXML getSQLXML(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSQLXML'");
    }

    @Test
    void updateSQLXML(final int columnIndex, final SQLXML xmlObject) throws SQLException {
        final SQLXML xml = mock(SQLXML.class);
        testee().updateSQLXML(COL_INDEX, xml);
        verify(resultSetMock).updateSQLXML(eq(COL_INDEX), same(xml));
    }

    @Test
    void updateSQLXML(final String columnLabel, final SQLXML xmlObject) throws SQLException {
        final SQLXML xml = mock(SQLXML.class);
        testee().updateSQLXML(COL_LABEL, xml);
        verify(resultSetMock).updateSQLXML(eq(COL_LABEL), same(xml));
    }

    @Test
    public String getNString(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNString'");
    }

    @Test
    public String getNString(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNString'");
    }

    @Test
    public Reader getNCharacterStream(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNCharacterStream'");
    }

    @Test
    public Reader getNCharacterStream(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNCharacterStream'");
    }

    @Test
    void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNCharacterStream(COL_INDEX, reader, 3L);
        verify(resultSetMock).updateNCharacterStream(eq(COL_INDEX), same(reader), eq(3L));
    }

    @Test
    void updateNCharacterStream(final String columnLabel, final Reader r, final long length)
            throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNCharacterStream(COL_LABEL, reader, 3L);
        verify(resultSetMock).updateNCharacterStream(eq(COL_LABEL), same(reader), eq(3L));
    }

    @Test
    void updateAsciiStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateAsciiStream(COL_INDEX, stream, 3L);
        verify(resultSetMock).updateAsciiStream(eq(COL_INDEX), same(stream), eq(3L));
    }

    @Test
    void updateBinaryStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBinaryStream(COL_INDEX, stream, 3L);
        verify(resultSetMock).updateBinaryStream(eq(COL_INDEX), same(stream), eq(3L));
    }

    @Test
    void updateCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateCharacterStream(COL_INDEX, reader, 3L);
        verify(resultSetMock).updateCharacterStream(eq(COL_INDEX), same(reader), eq(3L));
    }

    @Test
    void updateAsciiStream(final String columnLabel, final InputStream x, final long length)
            throws SQLException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateAsciiStream(COL_LABEL, stream, 3L);
        verify(resultSetMock).updateAsciiStream(eq(COL_LABEL), same(stream), eq(3L));
    }

    @Test
    void updateBinaryStream(final String columnLabel, final InputStream x, final long length)
            throws SQLException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBinaryStream(COL_LABEL, stream, 3L);
        verify(resultSetMock).updateBinaryStream(eq(COL_LABEL), same(stream), eq(3L));
    }

    @Test
    void updateCharacterStream(final String columnLabel, final Reader r, final long length)
            throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateCharacterStream(COL_LABEL, reader, 3L);
        verify(resultSetMock).updateCharacterStream(eq(COL_LABEL), same(reader), eq(3L));
    }

    @Test
    void updateBlob(final int columnIndex, final InputStream inputStream, final long length)
            throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBlob(COL_INDEX, stream, 3L);
        verify(resultSetMock).updateBlob(eq(COL_INDEX), same(stream), eq(3L));
    }

    @Test
    void updateBlob(final String columnLabel, final InputStream inputStream, final long length)
            throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBlob(COL_LABEL, stream, 3L);
        verify(resultSetMock).updateBlob(eq(COL_LABEL), same(stream), eq(3L));
    }

    @Test
    void updateClob(final int columnIndex, final Reader r, final long length) throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateClob(COL_INDEX, reader, 3L);
        verify(resultSetMock).updateClob(eq(COL_INDEX), same(reader), eq(3L));
    }

    @Test
    void updateClob(final String columnLabel, final Reader r, final long length) throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateClob(COL_LABEL, reader, 3L);
        verify(resultSetMock).updateClob(eq(COL_LABEL), same(reader), eq(3L));
    }

    @Test
    void updateNClob(final int columnIndex, final Reader r, final long length) throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNClob(COL_INDEX, reader, 3L);
        verify(resultSetMock).updateNClob(eq(COL_INDEX), same(reader), eq(3L));
    }

    @Test
    void updateNClob(final String columnLabel, final Reader r, final long length) throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNClob(COL_LABEL, reader, 3L);
        verify(resultSetMock).updateNClob(eq(COL_LABEL), same(reader), eq(3L));
    }

    @Test
    void updateNCharacterStream(final int columnIndex, final Reader x) throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNCharacterStream(COL_INDEX, reader);
        verify(resultSetMock).updateNCharacterStream(eq(COL_INDEX), same(reader));
    }

    @Test
    void updateNCharacterStream(final String columnLabel, final Reader r) throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNCharacterStream(COL_LABEL, reader);
        verify(resultSetMock).updateNCharacterStream(eq(COL_LABEL), same(reader));
    }

    @Test
    void updateAsciiStream(final int columnIndex, final InputStream x) throws SQLException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateAsciiStream(COL_INDEX, stream);
        verify(resultSetMock).updateAsciiStream(eq(COL_INDEX), same(stream));
    }

    @Test
    void updateBinaryStream(final int columnIndex, final InputStream x) throws SQLException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBinaryStream(COL_INDEX, stream);
        verify(resultSetMock).updateBinaryStream(eq(COL_INDEX), same(stream));
    }

    @Test
    void updateCharacterStream(final int columnIndex, final Reader x) throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateCharacterStream(COL_INDEX, reader);
        verify(resultSetMock).updateCharacterStream(eq(COL_INDEX), same(reader));
    }

    @Test
    void updateAsciiStream(final String columnLabel, final InputStream x) throws SQLException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateAsciiStream(COL_LABEL, stream);
        verify(resultSetMock).updateAsciiStream(eq(COL_LABEL), same(stream));
    }

    @Test
    void updateBinaryStream(final String columnLabel, final InputStream x) throws SQLException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBinaryStream(COL_LABEL, stream);
        verify(resultSetMock).updateBinaryStream(eq(COL_LABEL), same(stream));
    }

    @Test
    void updateCharacterStream(final String columnLabel, final Reader r) throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateCharacterStream(COL_LABEL, reader);
        verify(resultSetMock).updateCharacterStream(eq(COL_LABEL), same(reader));
    }

    @Test
    void updateBlob(final int columnIndex, final InputStream inputStream) throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBlob(COL_INDEX, stream);
        verify(resultSetMock).updateBlob(eq(COL_INDEX), same(stream));
    }

    @Test
    void updateBlob(final String columnLabel, final InputStream inputStream) throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBlob(COL_LABEL, stream);
        verify(resultSetMock).updateBlob(eq(COL_LABEL), same(stream));
    }

    @Test
    void updateClob(final int columnIndex, final Reader r) throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateClob(COL_INDEX, reader);
        verify(resultSetMock).updateClob(eq(COL_INDEX), same(reader));
    }

    @Test
    void updateClob(final String columnLabel, final Reader r) throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateClob(COL_LABEL, reader);
        verify(resultSetMock).updateClob(eq(COL_LABEL), same(reader));
    }

    @Test
    void updateNClob(final int columnIndex, final Reader r) throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNClob(COL_INDEX, reader);
        verify(resultSetMock).updateNClob(eq(COL_INDEX), same(reader));
    }

    @Test
    void updateNClob(final String columnLabel, final Reader r) throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNClob(COL_LABEL, reader);
        verify(resultSetMock).updateNClob(eq(COL_LABEL), same(reader));
    }

    @Test
    public <T> T getObject(final int columnIndex, final Class<T> type) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getObject'");
    }

    @Test
    public <T> T getObject(final String columnLabel, final Class<T> type) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getObject'");
    }

    ResultSet testee() {
        return new DelegatingResultSet(resultSetMock);
    }
}
