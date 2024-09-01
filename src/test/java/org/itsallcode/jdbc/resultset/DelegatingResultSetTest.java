package org.itsallcode.jdbc.resultset;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

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
        assertTrue(testee().isWrapperFor(String.class));
    }

    @Test
    void next() throws SQLException {
        when(resultSetMock.next()).thenReturn(true);
        assertTrue(testee().next());
    }

    @Test
    void close() throws SQLException {
        testee().close();
        verify(resultSetMock).close();
    }

    @Test
    void wasNull() throws SQLException {
        when(resultSetMock.wasNull()).thenReturn(true);
        assertTrue(testee().wasNull());
    }

    @Test
    void getStringIndex() throws SQLException {
        when(resultSetMock.getString(COL_INDEX)).thenReturn("c");
        assertEquals("c", testee().getString(COL_INDEX));
    }

    @Test
    void getBooleanIndex() throws SQLException {
        when(resultSetMock.getBoolean(1)).thenReturn(true);
        assertTrue(testee().getBoolean(1));
    }

    @Test
    void getByteIndex() throws SQLException {
        when(resultSetMock.getByte(COL_INDEX)).thenReturn((byte) 2);
        assertEquals((byte) 2, testee().getByte(COL_INDEX));
    }

    @Test
    void getShortIndex() throws SQLException {
        when(resultSetMock.getShort(COL_INDEX)).thenReturn((short) 2);
        assertEquals((short) 2, testee().getShort(COL_INDEX));
    }

    @Test
    void getIntIndex() throws SQLException {
        when(resultSetMock.getInt(COL_INDEX)).thenReturn(2);
        assertEquals(2, testee().getInt(COL_INDEX));
    }

    @Test
    void getLongIndex() throws SQLException {
        when(resultSetMock.getLong(COL_INDEX)).thenReturn(2L);
        assertEquals(2L, testee().getLong(COL_INDEX));
    }

    @Test
    void getFloatIndex() throws SQLException {
        when(resultSetMock.getFloat(COL_INDEX)).thenReturn(2.2F);
        assertEquals(2.2F, testee().getFloat(COL_INDEX));
    }

    @Test
    void getDoubleIndex() throws SQLException {
        when(resultSetMock.getDouble(COL_INDEX)).thenReturn(2.2);
        assertEquals(2.2, testee().getDouble(COL_INDEX));
    }

    @Test
    @SuppressWarnings("deprecation")
    void getBigDecimalIndexScale() throws SQLException {
        when(resultSetMock.getBigDecimal(COL_INDEX, 2)).thenReturn(BigDecimal.TEN);
        assertEquals(BigDecimal.TEN, testee().getBigDecimal(COL_INDEX, 2));
    }

    @Test
    void getBytesIndex() throws SQLException {
        final byte[] value = new byte[] { 2 };
        when(resultSetMock.getBytes(COL_INDEX)).thenReturn(value);
        assertSame(value, testee().getBytes(COL_INDEX));
    }

    @Test
    void getDateIndex() throws SQLException {
        when(resultSetMock.getDate(COL_INDEX)).thenReturn(new Date(3));
        assertEquals(new Date(3), testee().getDate(COL_INDEX));
    }

    @Test
    void getTimeIndex() throws SQLException {
        when(resultSetMock.getTime(COL_INDEX)).thenReturn(new Time(3));
        assertEquals(new Time(3), testee().getTime(COL_INDEX));
    }

    @Test
    void getTimestampIndex() throws SQLException {
        when(resultSetMock.getTimestamp(COL_INDEX)).thenReturn(new Timestamp(3));
        assertEquals(new Timestamp(3), testee().getTimestamp(COL_INDEX));
    }

    @Test
    void getAsciiStreamIndex() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        when(resultSetMock.getAsciiStream(COL_INDEX)).thenReturn(stream);
        assertSame(stream, testee().getAsciiStream(COL_INDEX));
    }

    @Test
    @SuppressWarnings("deprecation")
    void getUnicodeStreamIndex() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        when(resultSetMock.getUnicodeStream(COL_INDEX)).thenReturn(stream);
        assertSame(stream, testee().getUnicodeStream(COL_INDEX));
    }

    @Test
    void getBinaryStreamIndex() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        when(resultSetMock.getBinaryStream(COL_INDEX)).thenReturn(stream);
        assertSame(stream, testee().getBinaryStream(COL_INDEX));

    }

    @Test
    void getStringLabel() throws SQLException {
        when(resultSetMock.getString(COL_LABEL)).thenReturn("c");
        assertEquals("c", testee().getString(COL_LABEL));
    }

    @Test
    void getBoolean() throws SQLException {
        when(resultSetMock.getBoolean("a")).thenReturn(true);
        assertTrue(testee().getBoolean("a"));
    }

    @Test
    void getByteLabel() throws SQLException {
        when(resultSetMock.getByte(COL_LABEL)).thenReturn((byte) 2);
        assertEquals((byte) 2, testee().getByte(COL_LABEL));
    }

    @Test
    void getShortLabel() throws SQLException {
        when(resultSetMock.getShort(COL_LABEL)).thenReturn((short) 2);
        assertEquals((short) 2, testee().getShort(COL_LABEL));
    }

    @Test
    void getIntLabel() throws SQLException {
        when(resultSetMock.getInt(COL_LABEL)).thenReturn(2);
        assertEquals(2, testee().getInt(COL_LABEL));
    }

    @Test
    void getLongLabel() throws SQLException {
        when(resultSetMock.getLong(COL_LABEL)).thenReturn(2L);
        assertEquals(2L, testee().getLong(COL_LABEL));
    }

    @Test
    void getFloatLabel() throws SQLException {
        when(resultSetMock.getFloat(COL_LABEL)).thenReturn(2.2F);
        assertEquals(2.2F, testee().getFloat(COL_LABEL));
    }

    @Test
    void getDoubleLabel() throws SQLException {
        when(resultSetMock.getDouble(COL_LABEL)).thenReturn(2.2);
        assertEquals(2.2, testee().getDouble(COL_LABEL));
    }

    @Test
    @SuppressWarnings("deprecation")
    void getBigDecimalLabelScale() throws SQLException {
        when(resultSetMock.getBigDecimal(COL_LABEL, 2)).thenReturn(BigDecimal.TEN);
        assertEquals(BigDecimal.TEN, testee().getBigDecimal(COL_LABEL, 2));
    }

    @Test
    void getBytesLabel() throws SQLException {
        final byte[] value = new byte[] { 2 };
        when(resultSetMock.getBytes(COL_LABEL)).thenReturn(value);
        assertSame(value, testee().getBytes(COL_LABEL));
    }

    @Test
    void getDateLabel() throws SQLException {
        when(resultSetMock.getDate(COL_LABEL)).thenReturn(new Date(3));
        assertEquals(new Date(3), testee().getDate(COL_LABEL));
    }

    @Test
    void getTimeLabel() throws SQLException {
        when(resultSetMock.getTime(COL_LABEL)).thenReturn(new Time(3));
        assertEquals(new Time(3), testee().getTime(COL_LABEL));
    }

    @Test
    void getTimestampLabel() throws SQLException {
        when(resultSetMock.getTimestamp(COL_LABEL)).thenReturn(new Timestamp(3));
        assertEquals(new Timestamp(3), testee().getTimestamp(COL_LABEL));
    }

    @Test
    void getAsciiStreamLabel() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        when(resultSetMock.getAsciiStream(COL_LABEL)).thenReturn(stream);
        assertSame(stream, testee().getAsciiStream(COL_LABEL));
    }

    @Test
    @SuppressWarnings("deprecation")
    void getUnicodeStreamLabel() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        when(resultSetMock.getUnicodeStream(COL_LABEL)).thenReturn(stream);
        assertSame(stream, testee().getUnicodeStream(COL_LABEL));
    }

    @Test
    void getBinaryStreamLabel() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        when(resultSetMock.getBinaryStream(COL_LABEL)).thenReturn(stream);
        assertSame(stream, testee().getBinaryStream(COL_LABEL));
    }

    @Test
    void getWarnings() throws SQLException {
        final SQLWarning warning = mock(SQLWarning.class);
        when(resultSetMock.getWarnings()).thenReturn(warning);
        assertSame(warning, testee().getWarnings());
    }

    @Test
    void clearWarnings() throws SQLException {
        testee().clearWarnings();
        verify(resultSetMock).clearWarnings();
    }

    @Test
    void getCursorName() throws SQLException {
        when(resultSetMock.getCursorName()).thenReturn("c");
        assertEquals("c", testee().getCursorName());

    }

    @Test
    void getMetaData() throws SQLException {
        final ResultSetMetaData metadata = mock(ResultSetMetaData.class);
        when(resultSetMock.getMetaData()).thenReturn(metadata);
        assertSame(metadata, testee().getMetaData());
    }

    @Test
    void getObjectIndex() throws SQLException {
        final Object object = new Object();
        when(resultSetMock.getObject(COL_INDEX)).thenReturn(object);
        assertSame(object, testee().getObject(COL_INDEX));
    }

    @Test
    void getObjectLabel() throws SQLException {
        final Object object = new Object();
        when(resultSetMock.getObject(COL_LABEL)).thenReturn(object);
        assertSame(object, testee().getObject(COL_LABEL));
    }

    @Test
    void findColumnLabel() throws SQLException {
        when(resultSetMock.findColumn(COL_LABEL)).thenReturn(2);
        assertEquals(2, testee().findColumn(COL_LABEL));
    }

    @Test
    void getCharacterStreamIndex() throws SQLException {
        final Reader reader = new StringReader("s");
        when(resultSetMock.getCharacterStream(COL_INDEX)).thenReturn(reader);
        assertSame(reader, testee().getCharacterStream(COL_INDEX));
    }

    @Test
    void getCharacterStreamLabel() throws SQLException {
        final Reader reader = new StringReader("s");
        when(resultSetMock.getCharacterStream(COL_LABEL)).thenReturn(reader);
        assertSame(reader, testee().getCharacterStream(COL_LABEL));
    }

    @Test
    void getBigDecimalIndex() throws SQLException {
        when(resultSetMock.getBigDecimal(COL_INDEX)).thenReturn(BigDecimal.TEN);
        assertEquals(BigDecimal.TEN, testee().getBigDecimal(COL_INDEX));
    }

    @Test
    void getBigDecimalLabel() throws SQLException {
        when(resultSetMock.getBigDecimal(COL_LABEL)).thenReturn(BigDecimal.TEN);
        assertEquals(BigDecimal.TEN, testee().getBigDecimal(COL_LABEL));
    }

    @Test
    void isBeforeFirst() throws SQLException {
        when(resultSetMock.isBeforeFirst()).thenReturn(true);
        assertTrue(testee().isBeforeFirst());
    }

    @Test
    void isAfterLast() throws SQLException {
        when(resultSetMock.isAfterLast()).thenReturn(true);
        assertTrue(testee().isAfterLast());
    }

    @Test
    void isFirst() throws SQLException {
        when(resultSetMock.isFirst()).thenReturn(true);
        assertTrue(testee().isFirst());
    }

    @Test
    void isLast() throws SQLException {
        when(resultSetMock.isLast()).thenReturn(true);
        assertTrue(testee().isLast());
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
        assertTrue(testee().first());
    }

    @Test
    void last() throws SQLException {
        when(resultSetMock.last()).thenReturn(true);
        assertTrue(testee().last());
    }

    @Test
    void getRow() throws SQLException {
        when(resultSetMock.getRow()).thenReturn(1);
        assertEquals(1, testee().getRow());
    }

    @Test
    void absolute() throws SQLException {
        when(resultSetMock.absolute(1)).thenReturn(true);
        assertTrue(testee().absolute(1));
    }

    @Test
    void relative() throws SQLException {
        when(resultSetMock.relative(1)).thenReturn(true);
        assertTrue(testee().relative(1));
    }

    @Test
    void previous() throws SQLException {
        when(resultSetMock.previous()).thenReturn(true);
        assertTrue(testee().previous());
    }

    @Test
    void setFetchDirection() throws SQLException {
        testee().setFetchDirection(1);
        verify(resultSetMock).setFetchDirection(1);
    }

    @Test
    void getFetchDirection() throws SQLException {
        when(resultSetMock.getFetchDirection()).thenReturn(1);
        assertEquals(1, testee().getFetchDirection());
    }

    @Test
    void setFetchSize() throws SQLException {
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
        assertTrue(testee().rowUpdated());
    }

    @Test
    void rowInserted() throws SQLException {
        when(resultSetMock.rowInserted()).thenReturn(true);
        assertTrue(testee().rowInserted());
    }

    @Test
    void rowDeleted() throws SQLException {
        when(resultSetMock.rowDeleted()).thenReturn(true);
        assertTrue(testee().rowDeleted());
    }

    @Test
    void updateNullIndex() throws SQLException {
        testee().updateNull(1);
        verify(resultSetMock).updateNull(1);
    }

    @Test
    void updateBooleanIndex() throws SQLException {
        testee().updateBoolean(COL_INDEX, true);
        verify(resultSetMock).updateBoolean(COL_INDEX, true);
    }

    @Test
    void updateByteIndex() throws SQLException {
        testee().updateByte(COL_INDEX, (byte) 2);
        verify(resultSetMock).updateByte(COL_INDEX, (byte) 2);
    }

    @Test
    void updateShortIndex() throws SQLException {
        testee().updateShort(COL_INDEX, (short) 2);
        verify(resultSetMock).updateShort(COL_INDEX, (short) 2);
    }

    @Test
    void updateIntIndex() throws SQLException {
        testee().updateInt(COL_INDEX, 2);
        verify(resultSetMock).updateInt(COL_INDEX, 2);
    }

    @Test
    void updateLongIndex() throws SQLException {
        testee().updateLong(COL_INDEX, 2);
        verify(resultSetMock).updateLong(COL_INDEX, 2);
    }

    @Test
    void updateFloatIndex() throws SQLException {
        testee().updateFloat(COL_INDEX, 2.2F);
        verify(resultSetMock).updateFloat(COL_INDEX, 2.2F);
    }

    @Test
    void updateDoubleIndex() throws SQLException {
        testee().updateDouble(COL_INDEX, 2.2);
        verify(resultSetMock).updateDouble(COL_INDEX, 2.2);
    }

    @Test
    void updateBigDecimalIndex() throws SQLException {
        testee().updateBigDecimal(COL_INDEX, BigDecimal.TEN);
        verify(resultSetMock).updateBigDecimal(COL_INDEX, BigDecimal.TEN);
    }

    @Test
    void updateStringIndex() throws SQLException {
        testee().updateString(COL_INDEX, "a");
        verify(resultSetMock).updateString(COL_INDEX, "a");
    }

    @Test
    void updateBytesIndex() throws SQLException {
        testee().updateBytes(COL_INDEX, new byte[] { 1 });
        verify(resultSetMock).updateBytes(COL_INDEX, new byte[] { 1 });
    }

    @Test
    void updateDateIndex() throws SQLException {
        testee().updateDate(COL_INDEX, new Date(2));
        verify(resultSetMock).updateDate(COL_INDEX, new Date(2));
    }

    @Test
    void updateTimeIndex() throws SQLException {
        testee().updateTime(COL_INDEX, new Time(2));
        verify(resultSetMock).updateTime(COL_INDEX, new Time(2));
    }

    @Test
    void updateTimestampIndex() throws SQLException {
        testee().updateTimestamp(COL_INDEX, new Timestamp(2));
        verify(resultSetMock).updateTimestamp(COL_INDEX, new Timestamp(2));
    }

    @Test
    void updateAsciiStreamIndexInt() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateAsciiStream(COL_INDEX, stream, 3);
        verify(resultSetMock).updateAsciiStream(eq(COL_INDEX), same(stream), eq(3));
    }

    @Test
    void updateBinaryStreamIndexInt() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBinaryStream(COL_INDEX, stream, 3);
        verify(resultSetMock).updateBinaryStream(eq(COL_INDEX), same(stream), eq(3));
    }

    @Test
    void updateCharacterStreamIndexInt() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateCharacterStream(COL_INDEX, reader, 3);
        verify(resultSetMock).updateCharacterStream(eq(COL_INDEX), same(reader), eq(3));
    }

    @Test
    void updateObjectIndexScaleOrLength() throws SQLException {
        final Object object = new Object();
        testee().updateObject(COL_INDEX, object, 3);
        verify(resultSetMock).updateObject(eq(COL_INDEX), same(object), eq(3));
    }

    @Test
    void updateObjectIndex() throws SQLException {
        final Object object = new Object();
        testee().updateObject(COL_INDEX, object);
        verify(resultSetMock).updateObject(eq(COL_INDEX), same(object));
    }

    @Test
    void updateNullLabel() throws SQLException {
        testee().updateNull("c");
        verify(resultSetMock).updateNull("c");
    }

    @Test
    void updateBooleanLabel() throws SQLException {
        testee().updateBoolean("c", true);
        verify(resultSetMock).updateBoolean("c", true);
    }

    @Test
    void updateByteLabel() throws SQLException {
        testee().updateByte("c", (byte) 2);
        verify(resultSetMock).updateByte("c", (byte) 2);
    }

    @Test
    void updateShortLabel() throws SQLException {
        testee().updateShort("c", (short) 2);
        verify(resultSetMock).updateShort("c", (short) 2);

    }

    @Test
    void updateIntLabel() throws SQLException {
        testee().updateInt("c", 2);
        verify(resultSetMock).updateInt("c", 2);
    }

    @Test
    void updateLongLabel() throws SQLException {
        testee().updateLong("c", 2);
        verify(resultSetMock).updateLong("c", 2);
    }

    @Test
    void updateFloatLabel() throws SQLException {
        testee().updateFloat(COL_LABEL, 2.2F);
        verify(resultSetMock).updateFloat(COL_LABEL, 2.2F);
    }

    @Test
    void updateDoubleLabel() throws SQLException {
        testee().updateDouble(COL_LABEL, 2.2);
        verify(resultSetMock).updateDouble(COL_LABEL, 2.2);
    }

    @Test
    void updateBigDecimalLabel() throws SQLException {
        testee().updateBigDecimal(COL_LABEL, BigDecimal.TEN);
        verify(resultSetMock).updateBigDecimal(COL_LABEL, BigDecimal.TEN);
    }

    @Test
    void updateStringLabel() throws SQLException {
        testee().updateString(COL_LABEL, "a");
        verify(resultSetMock).updateString(COL_LABEL, "a");
    }

    @Test
    void updateBytesLabel() throws SQLException {
        testee().updateBytes(COL_LABEL, new byte[] { 1 });
        verify(resultSetMock).updateBytes(COL_LABEL, new byte[] { 1 });
    }

    @Test
    void updateDateLabel() throws SQLException {
        testee().updateDate(COL_LABEL, new Date(2));
        verify(resultSetMock).updateDate(COL_LABEL, new Date(2));
    }

    @Test
    void updateTimeLabel() throws SQLException {
        testee().updateTime(COL_LABEL, new Time(2));
        verify(resultSetMock).updateTime(COL_LABEL, new Time(2));
    }

    @Test
    void updateTimestampLabel() throws SQLException {
        testee().updateTimestamp(COL_LABEL, new Timestamp(2));
        verify(resultSetMock).updateTimestamp(COL_LABEL, new Timestamp(2));
    }

    @Test
    void updateAsciiStreamLabelInt() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateAsciiStream(COL_LABEL, stream, 3);
        verify(resultSetMock).updateAsciiStream(eq(COL_LABEL), same(stream), eq(3));
    }

    @Test
    void updateBinaryStreamLabelInt()
            throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBinaryStream(COL_LABEL, stream, 3);
        verify(resultSetMock).updateBinaryStream(eq(COL_LABEL), same(stream), eq(3));
    }

    @Test
    void updateCharacterStreamLabelInt()
            throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateCharacterStream(COL_LABEL, reader, 3);
        verify(resultSetMock).updateCharacterStream(eq(COL_LABEL), same(reader), eq(3));
    }

    @Test
    void updateObjectLabelScaleOrLenght() throws SQLException {
        final Object object = new Object();
        testee().updateObject(COL_LABEL, object, 3);
        verify(resultSetMock).updateObject(eq(COL_LABEL), same(object), eq(3));
    }

    @Test
    void updateObjectLabel() throws SQLException {
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
    void getStatement() throws SQLException {
        final Statement stmt = mock(Statement.class);
        when(resultSetMock.getStatement()).thenReturn(stmt);
        assertSame(stmt, testee().getStatement());
    }

    @Test
    void getObjectIndexMap() throws SQLException {
        final Object object = new Object();
        final Map<String, Class<?>> map = new HashMap<>();
        when(resultSetMock.getObject(eq(COL_INDEX), same(map))).thenReturn(object);
        assertSame(object, testee().getObject(COL_INDEX, map));
    }

    @Test
    void getRefIndex() throws SQLException {
        final Ref ref = mock(Ref.class);
        when(resultSetMock.getRef(COL_INDEX)).thenReturn(ref);
        assertSame(ref, testee().getRef(COL_INDEX));
    }

    @Test
    void getBlobIndex() throws SQLException {
        final Blob blob = mock(Blob.class);
        when(resultSetMock.getBlob(COL_INDEX)).thenReturn(blob);
        assertSame(blob, testee().getBlob(COL_INDEX));
    }

    @Test
    void getClobIndex() throws SQLException {
        final Clob clob = mock(Clob.class);
        when(resultSetMock.getClob(COL_INDEX)).thenReturn(clob);
        assertSame(clob, testee().getClob(COL_INDEX));
    }

    @Test
    void getArrayIndex() throws SQLException {
        final Array array = mock(Array.class);
        when(resultSetMock.getArray(COL_INDEX)).thenReturn(array);
        assertSame(array, testee().getArray(COL_INDEX));
    }

    @Test
    void getObjectLabelMap() throws SQLException {
        final Object object = new Object();
        final Map<String, Class<?>> map = new HashMap<>();
        when(resultSetMock.getObject(eq(COL_LABEL), same(map))).thenReturn(object);
        assertSame(object, testee().getObject(COL_LABEL, map));
    }

    @Test
    void getRefLabel() throws SQLException {
        final Ref ref = mock(Ref.class);
        when(resultSetMock.getRef(COL_LABEL)).thenReturn(ref);
        assertSame(ref, testee().getRef(COL_LABEL));

    }

    @Test
    void getBlobLabel() throws SQLException {
        final Blob blob = mock(Blob.class);
        when(resultSetMock.getBlob(COL_LABEL)).thenReturn(blob);
        assertSame(blob, testee().getBlob(COL_LABEL));

    }

    @Test
    void getClobLabel() throws SQLException {
        final Clob clob = mock(Clob.class);
        when(resultSetMock.getClob(COL_LABEL)).thenReturn(clob);
        assertSame(clob, testee().getClob(COL_LABEL));
    }

    @Test
    void getArrayLabel() throws SQLException {
        final Array array = mock(Array.class);
        when(resultSetMock.getArray(COL_LABEL)).thenReturn(array);
        assertSame(array, testee().getArray(COL_LABEL));
    }

    @Test
    void getDateIndexCal() throws SQLException {
        final Calendar cal = Calendar.getInstance();
        when(resultSetMock.getDate(eq(COL_INDEX), same(cal))).thenReturn(new Date(3));
        assertEquals(new Date(3), testee().getDate(COL_INDEX, cal));
    }

    @Test
    void getDateLabelCal() throws SQLException {
        final Calendar cal = Calendar.getInstance();
        when(resultSetMock.getDate(eq(COL_LABEL), same(cal))).thenReturn(new Date(3));
        assertEquals(new Date(3), testee().getDate(COL_LABEL, cal));
    }

    @Test
    void getTimeIndexCal() throws SQLException {
        final Calendar cal = Calendar.getInstance();
        when(resultSetMock.getTime(eq(COL_INDEX), same(cal))).thenReturn(new Time(3));
        assertEquals(new Time(3), testee().getTime(COL_INDEX, cal));
    }

    @Test
    void getTimeLabelCal() throws SQLException {
        final Calendar cal = Calendar.getInstance();
        when(resultSetMock.getTime(eq(COL_LABEL), same(cal))).thenReturn(new Time(3));
        assertEquals(new Time(3), testee().getTime(COL_LABEL, cal));
    }

    @Test
    void getTimestampIndexCal() throws SQLException {
        final Calendar cal = Calendar.getInstance();
        when(resultSetMock.getTimestamp(eq(COL_INDEX), same(cal))).thenReturn(new Timestamp(3));
        assertEquals(new Timestamp(3), testee().getTimestamp(COL_INDEX, cal));
    }

    @Test
    void getTimestampLabelCal() throws SQLException {
        final Calendar cal = Calendar.getInstance();
        when(resultSetMock.getTimestamp(eq(COL_LABEL), same(cal))).thenReturn(new Timestamp(3));
        assertEquals(new Timestamp(3), testee().getTimestamp(COL_LABEL, cal));
    }

    @Test
    void getURLIndex() throws SQLException, MalformedURLException, URISyntaxException {
        final URL url = new URI("https://example.com").toURL();
        when(resultSetMock.getURL(COL_INDEX)).thenReturn(url);
        assertSame(url, testee().getURL(COL_INDEX));
    }

    @Test
    void getURLLabel() throws SQLException, MalformedURLException, URISyntaxException {
        final URL url = new URI("https://example.com").toURL();
        when(resultSetMock.getURL(COL_LABEL)).thenReturn(url);
        assertSame(url, testee().getURL(COL_LABEL));
    }

    @Test
    void updateRefIndex() throws SQLException {
        final Ref ref = mock(Ref.class);
        testee().updateRef(COL_INDEX, ref);
        verify(resultSetMock).updateRef(eq(COL_INDEX), same(ref));
    }

    @Test
    void updateRefLabel() throws SQLException {
        final Ref ref = mock(Ref.class);
        testee().updateRef(COL_LABEL, ref);
        verify(resultSetMock).updateRef(eq(COL_LABEL), same(ref));
    }

    @Test
    void updateBlobIndexStream() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBlob(COL_INDEX, stream);
        verify(resultSetMock).updateBlob(eq(COL_INDEX), same(stream));
    }

    @Test
    void updateBlobIndexBlob() throws SQLException {
        final Blob blobMock = mock(Blob.class);
        testee().updateBlob(COL_INDEX, blobMock);
        verify(resultSetMock).updateBlob(eq(COL_INDEX), same(blobMock));
    }

    @Test
    void updateBlobLabelStream() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBlob(COL_LABEL, stream);
        verify(resultSetMock).updateBlob(eq(COL_LABEL), same(stream));
    }

    @Test
    void updateBlobLabelBlob() throws SQLException {
        final Blob blobMock = mock(Blob.class);
        testee().updateBlob(COL_LABEL, blobMock);
        verify(resultSetMock).updateBlob(eq(COL_LABEL), same(blobMock));
    }

    @Test
    void updateClobIndex() throws SQLException {
        final Clob clob = mock(Clob.class);
        testee().updateClob(COL_INDEX, clob);
        verify(resultSetMock).updateClob(eq(COL_INDEX), same(clob));
    }

    @Test
    void updateClobLabel() throws SQLException {
        final Clob clob = mock(Clob.class);
        testee().updateClob(COL_LABEL, clob);
        verify(resultSetMock).updateClob(eq(COL_LABEL), same(clob));
    }

    @Test
    void updateArrayIndex() throws SQLException {
        final Array array = mock(Array.class);
        testee().updateArray(COL_INDEX, array);
        verify(resultSetMock).updateArray(eq(COL_INDEX), same(array));
    }

    @Test
    void updateArrayLabel() throws SQLException {
        final Array array = mock(Array.class);
        testee().updateArray(COL_LABEL, array);
        verify(resultSetMock).updateArray(eq(COL_LABEL), same(array));
    }

    @Test
    void getRowIdIndex() throws SQLException {
        final RowId rowId = mock(RowId.class);
        when(resultSetMock.getRowId(COL_INDEX)).thenReturn(rowId);
        assertSame(rowId, testee().getRowId(COL_INDEX));
    }

    @Test
    void getRowIdLabel() throws SQLException {
        final RowId rowId = mock(RowId.class);
        when(resultSetMock.getRowId(COL_LABEL)).thenReturn(rowId);
        assertSame(rowId, testee().getRowId(COL_LABEL));
    }

    @Test
    void updateRowIdIndex() throws SQLException {
        final RowId rowId = mock(RowId.class);
        testee().updateRowId(COL_INDEX, rowId);
        verify(resultSetMock).updateRowId(eq(COL_INDEX), same(rowId));
    }

    @Test
    void updateRowIdLabel() throws SQLException {
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
        assertTrue(testee().isClosed());
    }

    @Test
    void updateNStringIndex() throws SQLException {
        testee().updateNString(COL_INDEX, "a");
        verify(resultSetMock).updateNString(COL_INDEX, "a");
    }

    @Test
    void updateNStringLabel() throws SQLException {
        testee().updateNString(COL_LABEL, "a");
        verify(resultSetMock).updateNString(COL_LABEL, "a");
    }

    @Test
    void updateNClobIndex() throws SQLException {
        final NClob clob = mock(NClob.class);
        testee().updateNClob(COL_INDEX, clob);
        verify(resultSetMock).updateNClob(eq(COL_INDEX), same(clob));
    }

    @Test
    void updateNClobLabel() throws SQLException {
        final NClob clob = mock(NClob.class);
        testee().updateNClob(COL_LABEL, clob);
        verify(resultSetMock).updateNClob(eq(COL_LABEL), same(clob));
    }

    @Test
    void getNClobIndex() throws SQLException {
        final NClob clob = mock(NClob.class);
        when(resultSetMock.getNClob(COL_INDEX)).thenReturn(clob);
        assertSame(clob, testee().getNClob(COL_INDEX));
    }

    @Test
    void getNClobLabel() throws SQLException {
        final NClob clob = mock(NClob.class);
        when(resultSetMock.getNClob(COL_LABEL)).thenReturn(clob);
        assertSame(clob, testee().getNClob(COL_LABEL));
    }

    @Test
    void getSQLXMLIndex() throws SQLException {
        final SQLXML xml = mock(SQLXML.class);
        when(resultSetMock.getSQLXML(COL_INDEX)).thenReturn(xml);
        assertSame(xml, testee().getSQLXML(COL_INDEX));
    }

    @Test
    void getSQLXMLLabel() throws SQLException {
        final SQLXML xml = mock(SQLXML.class);
        when(resultSetMock.getSQLXML(COL_LABEL)).thenReturn(xml);
        assertSame(xml, testee().getSQLXML(COL_LABEL));
    }

    @Test
    void updateSQLXMLIndex() throws SQLException {
        final SQLXML xml = mock(SQLXML.class);
        testee().updateSQLXML(COL_INDEX, xml);
        verify(resultSetMock).updateSQLXML(eq(COL_INDEX), same(xml));
    }

    @Test
    void updateSQLXMLLabel() throws SQLException {
        final SQLXML xml = mock(SQLXML.class);
        testee().updateSQLXML(COL_LABEL, xml);
        verify(resultSetMock).updateSQLXML(eq(COL_LABEL), same(xml));
    }

    @Test
    void getNStringIndex() throws SQLException {
        when(resultSetMock.getNString(COL_INDEX)).thenReturn("c");
        assertEquals("c", testee().getNString(COL_INDEX));
    }

    @Test
    void getNStringLabel() throws SQLException {
        when(resultSetMock.getNString(COL_LABEL)).thenReturn("c");
        assertEquals("c", testee().getNString(COL_LABEL));
    }

    @Test
    void getNCharacterStreamIndex() throws SQLException {
        final Reader reader = new StringReader("s");
        when(resultSetMock.getNCharacterStream(COL_INDEX)).thenReturn(reader);
        assertSame(reader, testee().getNCharacterStream(COL_INDEX));
    }

    @Test
    void getNCharacterStreamLabel() throws SQLException {
        final Reader reader = new StringReader("s");
        when(resultSetMock.getNCharacterStream(COL_LABEL)).thenReturn(reader);
        assertSame(reader, testee().getNCharacterStream(COL_LABEL));
    }

    @Test
    void updateNCharacterStreamIndexLong() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNCharacterStream(COL_INDEX, reader, 3L);
        verify(resultSetMock).updateNCharacterStream(eq(COL_INDEX), same(reader), eq(3L));
    }

    @Test
    void updateNCharacterStreamLabelLong() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNCharacterStream(COL_LABEL, reader, 3L);
        verify(resultSetMock).updateNCharacterStream(eq(COL_LABEL), same(reader), eq(3L));
    }

    @Test
    void updateAsciiStreamIndexLong() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateAsciiStream(COL_INDEX, stream, 3L);
        verify(resultSetMock).updateAsciiStream(eq(COL_INDEX), same(stream), eq(3L));
    }

    @Test
    void updateBinaryStreamIndexLong() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBinaryStream(COL_INDEX, stream, 3L);
        verify(resultSetMock).updateBinaryStream(eq(COL_INDEX), same(stream), eq(3L));
    }

    @Test
    void updateCharacterStreamIndexLong() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateCharacterStream(COL_INDEX, reader, 3L);
        verify(resultSetMock).updateCharacterStream(eq(COL_INDEX), same(reader), eq(3L));
    }

    @Test
    void updateAsciiStreamLabelLong() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateAsciiStream(COL_LABEL, stream, 3L);
        verify(resultSetMock).updateAsciiStream(eq(COL_LABEL), same(stream), eq(3L));
    }

    @Test
    void updateBinaryStreamLabelLong() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBinaryStream(COL_LABEL, stream, 3L);
        verify(resultSetMock).updateBinaryStream(eq(COL_LABEL), same(stream), eq(3L));
    }

    @Test
    void updateCharacterStreamLabelLong() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateCharacterStream(COL_LABEL, reader, 3L);
        verify(resultSetMock).updateCharacterStream(eq(COL_LABEL), same(reader), eq(3L));
    }

    @Test
    void updateBlobIndexLong() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBlob(COL_INDEX, stream, 3L);
        verify(resultSetMock).updateBlob(eq(COL_INDEX), same(stream), eq(3L));
    }

    @Test
    void updateBlobLabelLong() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBlob(COL_LABEL, stream, 3L);
        verify(resultSetMock).updateBlob(eq(COL_LABEL), same(stream), eq(3L));
    }

    @Test
    void updateClobIndexLong() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateClob(COL_INDEX, reader, 3L);
        verify(resultSetMock).updateClob(eq(COL_INDEX), same(reader), eq(3L));
    }

    @Test
    void updateClobLabelLong() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateClob(COL_LABEL, reader, 3L);
        verify(resultSetMock).updateClob(eq(COL_LABEL), same(reader), eq(3L));
    }

    @Test
    void updateNClobIndexLong() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNClob(COL_INDEX, reader, 3L);
        verify(resultSetMock).updateNClob(eq(COL_INDEX), same(reader), eq(3L));
    }

    @Test
    void updateNClobLabelLong() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNClob(COL_LABEL, reader, 3L);
        verify(resultSetMock).updateNClob(eq(COL_LABEL), same(reader), eq(3L));
    }

    @Test
    void updateNCharacterStreamIndex() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNCharacterStream(COL_INDEX, reader);
        verify(resultSetMock).updateNCharacterStream(eq(COL_INDEX), same(reader));
    }

    @Test
    void updateNCharacterStreamLabel() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNCharacterStream(COL_LABEL, reader);
        verify(resultSetMock).updateNCharacterStream(eq(COL_LABEL), same(reader));
    }

    @Test
    void updateAsciiStreamIndex() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateAsciiStream(COL_INDEX, stream);
        verify(resultSetMock).updateAsciiStream(eq(COL_INDEX), same(stream));
    }

    @Test
    void updateBinaryStreamIndex() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBinaryStream(COL_INDEX, stream);
        verify(resultSetMock).updateBinaryStream(eq(COL_INDEX), same(stream));
    }

    @Test
    void updateCharacterStreamIndex() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateCharacterStream(COL_INDEX, reader);
        verify(resultSetMock).updateCharacterStream(eq(COL_INDEX), same(reader));
    }

    @Test
    void updateAsciiStreamLabel() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateAsciiStream(COL_LABEL, stream);
        verify(resultSetMock).updateAsciiStream(eq(COL_LABEL), same(stream));
    }

    @Test
    void updateBinaryStreamLabel() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBinaryStream(COL_LABEL, stream);
        verify(resultSetMock).updateBinaryStream(eq(COL_LABEL), same(stream));
    }

    @Test
    void updateCharacterStreamLabel() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateCharacterStream(COL_LABEL, reader);
        verify(resultSetMock).updateCharacterStream(eq(COL_LABEL), same(reader));
    }

    @Test
    void updateBlobIndexNoLength() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBlob(COL_INDEX, stream);
        verify(resultSetMock).updateBlob(eq(COL_INDEX), same(stream));
    }

    @Test
    void updateBlobLabelNoLength() throws SQLException {
        final InputStream stream = new ByteArrayInputStream(new byte[] { 2 });
        testee().updateBlob(COL_LABEL, stream);
        verify(resultSetMock).updateBlob(eq(COL_LABEL), same(stream));
    }

    @Test
    void updateClobIndexNoLength() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateClob(COL_INDEX, reader);
        verify(resultSetMock).updateClob(eq(COL_INDEX), same(reader));
    }

    @Test
    void updateClobLabelNoLength() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateClob(COL_LABEL, reader);
        verify(resultSetMock).updateClob(eq(COL_LABEL), same(reader));
    }

    @Test
    void updateNClobIndexNoLength() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNClob(COL_INDEX, reader);
        verify(resultSetMock).updateNClob(eq(COL_INDEX), same(reader));
    }

    @Test
    void updateNClobLabelNoLength() throws SQLException {
        final Reader reader = new StringReader("a");
        testee().updateNClob(COL_LABEL, reader);
        verify(resultSetMock).updateNClob(eq(COL_LABEL), same(reader));
    }

    @Test
    void getObjectIndexType() throws SQLException {
        when(resultSetMock.getObject(COL_INDEX, String.class)).thenReturn("s");
        assertEquals("s", testee().getObject(COL_INDEX, String.class));
    }

    @Test
    void getObjectLableType() throws SQLException {
        when(resultSetMock.getObject(COL_LABEL, String.class)).thenReturn("s");
        assertEquals("s", testee().getObject(COL_LABEL, String.class));
    }

    ResultSet testee() {
        return new DelegatingResultSet(resultSetMock);
    }
}
