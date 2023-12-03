package org.itsallcode.jdbc.resultset;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

class DelegatingResultSet implements ResultSet {

    private final ResultSet delegate;

    DelegatingResultSet(final ResultSet delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unwrap'");
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isWrapperFor'");
    }

    @Override
    public boolean next() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'next'");
    }

    @Override
    public void close() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'close'");
    }

    @Override
    public boolean wasNull() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'wasNull'");
    }

    @Override
    public String getString(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getString'");
    }

    @Override
    public boolean getBoolean(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBoolean'");
    }

    @Override
    public byte getByte(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByte'");
    }

    @Override
    public short getShort(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getShort'");
    }

    @Override
    public int getInt(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInt'");
    }

    @Override
    public long getLong(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLong'");
    }

    @Override
    public float getFloat(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFloat'");
    }

    @Override
    public double getDouble(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDouble'");
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBigDecimal'");
    }

    @Override
    public byte[] getBytes(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBytes'");
    }

    @Override
    public Date getDate(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDate'");
    }

    @Override
    public Time getTime(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTime'");
    }

    @Override
    public Timestamp getTimestamp(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTimestamp'");
    }

    @Override
    public InputStream getAsciiStream(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAsciiStream'");
    }

    @Override
    @Deprecated
    public InputStream getUnicodeStream(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUnicodeStream'");
    }

    @Override
    public InputStream getBinaryStream(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBinaryStream'");
    }

    @Override
    public String getString(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getString'");
    }

    @Override
    public boolean getBoolean(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBoolean'");
    }

    @Override
    public byte getByte(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByte'");
    }

    @Override
    public short getShort(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getShort'");
    }

    @Override
    public int getInt(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInt'");
    }

    @Override
    public long getLong(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLong'");
    }

    @Override
    public float getFloat(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFloat'");
    }

    @Override
    public double getDouble(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDouble'");
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(final String columnLabel, final int scale) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBigDecimal'");
    }

    @Override
    public byte[] getBytes(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBytes'");
    }

    @Override
    public Date getDate(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDate'");
    }

    @Override
    public Time getTime(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTime'");
    }

    @Override
    public Timestamp getTimestamp(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTimestamp'");
    }

    @Override
    public InputStream getAsciiStream(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAsciiStream'");
    }

    @Override
    @Deprecated
    public InputStream getUnicodeStream(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUnicodeStream'");
    }

    @Override
    public InputStream getBinaryStream(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBinaryStream'");
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getWarnings'");
    }

    @Override
    public void clearWarnings() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearWarnings'");
    }

    @Override
    public String getCursorName() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCursorName'");
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMetaData'");
    }

    @Override
    public Object getObject(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getObject'");
    }

    @Override
    public Object getObject(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getObject'");
    }

    @Override
    public int findColumn(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findColumn'");
    }

    @Override
    public Reader getCharacterStream(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCharacterStream'");
    }

    @Override
    public Reader getCharacterStream(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCharacterStream'");
    }

    @Override
    public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBigDecimal'");
    }

    @Override
    public BigDecimal getBigDecimal(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBigDecimal'");
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBeforeFirst'");
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isAfterLast'");
    }

    @Override
    public boolean isFirst() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isFirst'");
    }

    @Override
    public boolean isLast() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isLast'");
    }

    @Override
    public void beforeFirst() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'beforeFirst'");
    }

    @Override
    public void afterLast() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'afterLast'");
    }

    @Override
    public boolean first() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'first'");
    }

    @Override
    public boolean last() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'last'");
    }

    @Override
    public int getRow() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRow'");
    }

    @Override
    public boolean absolute(final int row) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'absolute'");
    }

    @Override
    public boolean relative(final int rows) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'relative'");
    }

    @Override
    public boolean previous() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'previous'");
    }

    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFetchDirection'");
    }

    @Override
    public int getFetchDirection() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFetchDirection'");
    }

    @Override
    public void setFetchSize(final int rows) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setFetchSize'");
    }

    @Override
    public int getFetchSize() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFetchSize'");
    }

    @Override
    public int getType() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getType'");
    }

    @Override
    public int getConcurrency() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getConcurrency'");
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rowUpdated'");
    }

    @Override
    public boolean rowInserted() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rowInserted'");
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rowDeleted'");
    }

    @Override
    public void updateNull(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNull'");
    }

    @Override
    public void updateBoolean(final int columnIndex, final boolean x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBoolean'");
    }

    @Override
    public void updateByte(final int columnIndex, final byte x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateByte'");
    }

    @Override
    public void updateShort(final int columnIndex, final short x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateShort'");
    }

    @Override
    public void updateInt(final int columnIndex, final int x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateInt'");
    }

    @Override
    public void updateLong(final int columnIndex, final long x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateLong'");
    }

    @Override
    public void updateFloat(final int columnIndex, final float x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateFloat'");
    }

    @Override
    public void updateDouble(final int columnIndex, final double x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateDouble'");
    }

    @Override
    public void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBigDecimal'");
    }

    @Override
    public void updateString(final int columnIndex, final String x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateString'");
    }

    @Override
    public void updateBytes(final int columnIndex, final byte[] x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBytes'");
    }

    @Override
    public void updateDate(final int columnIndex, final Date x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateDate'");
    }

    @Override
    public void updateTime(final int columnIndex, final Time x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateTime'");
    }

    @Override
    public void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateTimestamp'");
    }

    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAsciiStream'");
    }

    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBinaryStream'");
    }

    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCharacterStream'");
    }

    @Override
    public void updateObject(final int columnIndex, final Object x, final int scaleOrLength) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateObject'");
    }

    @Override
    public void updateObject(final int columnIndex, final Object x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateObject'");
    }

    @Override
    public void updateNull(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNull'");
    }

    @Override
    public void updateBoolean(final String columnLabel, final boolean x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBoolean'");
    }

    @Override
    public void updateByte(final String columnLabel, final byte x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateByte'");
    }

    @Override
    public void updateShort(final String columnLabel, final short x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateShort'");
    }

    @Override
    public void updateInt(final String columnLabel, final int x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateInt'");
    }

    @Override
    public void updateLong(final String columnLabel, final long x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateLong'");
    }

    @Override
    public void updateFloat(final String columnLabel, final float x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateFloat'");
    }

    @Override
    public void updateDouble(final String columnLabel, final double x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateDouble'");
    }

    @Override
    public void updateBigDecimal(final String columnLabel, final BigDecimal x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBigDecimal'");
    }

    @Override
    public void updateString(final String columnLabel, final String x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateString'");
    }

    @Override
    public void updateBytes(final String columnLabel, final byte[] x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBytes'");
    }

    @Override
    public void updateDate(final String columnLabel, final Date x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateDate'");
    }

    @Override
    public void updateTime(final String columnLabel, final Time x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateTime'");
    }

    @Override
    public void updateTimestamp(final String columnLabel, final Timestamp x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateTimestamp'");
    }

    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAsciiStream'");
    }

    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x, final int length)
            throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBinaryStream'");
    }

    @Override
    public void updateCharacterStream(final String columnLabel, final Reader reader, final int length)
            throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCharacterStream'");
    }

    @Override
    public void updateObject(final String columnLabel, final Object x, final int scaleOrLength) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateObject'");
    }

    @Override
    public void updateObject(final String columnLabel, final Object x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateObject'");
    }

    @Override
    public void insertRow() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insertRow'");
    }

    @Override
    public void updateRow() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateRow'");
    }

    @Override
    public void deleteRow() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteRow'");
    }

    @Override
    public void refreshRow() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'refreshRow'");
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cancelRowUpdates'");
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'moveToInsertRow'");
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'moveToCurrentRow'");
    }

    @Override
    public Statement getStatement() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getStatement'");
    }

    @Override
    public Object getObject(final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getObject'");
    }

    @Override
    public Ref getRef(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRef'");
    }

    @Override
    public Blob getBlob(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBlob'");
    }

    @Override
    public Clob getClob(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getClob'");
    }

    @Override
    public Array getArray(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArray'");
    }

    @Override
    public Object getObject(final String columnLabel, final Map<String, Class<?>> map) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getObject'");
    }

    @Override
    public Ref getRef(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRef'");
    }

    @Override
    public Blob getBlob(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBlob'");
    }

    @Override
    public Clob getClob(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getClob'");
    }

    @Override
    public Array getArray(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getArray'");
    }

    @Override
    public Date getDate(final int columnIndex, final Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDate'");
    }

    @Override
    public Date getDate(final String columnLabel, final Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDate'");
    }

    @Override
    public Time getTime(final int columnIndex, final Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTime'");
    }

    @Override
    public Time getTime(final String columnLabel, final Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTime'");
    }

    @Override
    public Timestamp getTimestamp(final int columnIndex, final Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTimestamp'");
    }

    @Override
    public Timestamp getTimestamp(final String columnLabel, final Calendar cal) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTimestamp'");
    }

    @Override
    public URL getURL(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getURL'");
    }

    @Override
    public URL getURL(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getURL'");
    }

    @Override
    public void updateRef(final int columnIndex, final Ref x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateRef'");
    }

    @Override
    public void updateRef(final String columnLabel, final Ref x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateRef'");
    }

    @Override
    public void updateBlob(final int columnIndex, final Blob x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBlob'");
    }

    @Override
    public void updateBlob(final String columnLabel, final Blob x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBlob'");
    }

    @Override
    public void updateClob(final int columnIndex, final Clob x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateClob'");
    }

    @Override
    public void updateClob(final String columnLabel, final Clob x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateClob'");
    }

    @Override
    public void updateArray(final int columnIndex, final Array x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateArray'");
    }

    @Override
    public void updateArray(final String columnLabel, final Array x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateArray'");
    }

    @Override
    public RowId getRowId(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRowId'");
    }

    @Override
    public RowId getRowId(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRowId'");
    }

    @Override
    public void updateRowId(final int columnIndex, final RowId x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateRowId'");
    }

    @Override
    public void updateRowId(final String columnLabel, final RowId x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateRowId'");
    }

    @Override
    public int getHoldability() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHoldability'");
    }

    @Override
    public boolean isClosed() throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isClosed'");
    }

    @Override
    public void updateNString(final int columnIndex, final String nString) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNString'");
    }

    @Override
    public void updateNString(final String columnLabel, final String nString) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNString'");
    }

    @Override
    public void updateNClob(final int columnIndex, final NClob nClob) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNClob'");
    }

    @Override
    public void updateNClob(final String columnLabel, final NClob nClob) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNClob'");
    }

    @Override
    public NClob getNClob(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNClob'");
    }

    @Override
    public NClob getNClob(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNClob'");
    }

    @Override
    public SQLXML getSQLXML(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSQLXML'");
    }

    @Override
    public SQLXML getSQLXML(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSQLXML'");
    }

    @Override
    public void updateSQLXML(final int columnIndex, final SQLXML xmlObject) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSQLXML'");
    }

    @Override
    public void updateSQLXML(final String columnLabel, final SQLXML xmlObject) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateSQLXML'");
    }

    @Override
    public String getNString(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNString'");
    }

    @Override
    public String getNString(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNString'");
    }

    @Override
    public Reader getNCharacterStream(final int columnIndex) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNCharacterStream'");
    }

    @Override
    public Reader getNCharacterStream(final String columnLabel) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNCharacterStream'");
    }

    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNCharacterStream'");
    }

    @Override
    public void updateNCharacterStream(final String columnLabel, final Reader reader, final long length)
            throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNCharacterStream'");
    }

    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAsciiStream'");
    }

    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBinaryStream'");
    }

    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCharacterStream'");
    }

    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x, final long length)
            throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAsciiStream'");
    }

    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x, final long length)
            throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBinaryStream'");
    }

    @Override
    public void updateCharacterStream(final String columnLabel, final Reader reader, final long length)
            throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCharacterStream'");
    }

    @Override
    public void updateBlob(final int columnIndex, final InputStream inputStream, final long length)
            throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBlob'");
    }

    @Override
    public void updateBlob(final String columnLabel, final InputStream inputStream, final long length)
            throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBlob'");
    }

    @Override
    public void updateClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateClob'");
    }

    @Override
    public void updateClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateClob'");
    }

    @Override
    public void updateNClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNClob'");
    }

    @Override
    public void updateNClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNClob'");
    }

    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNCharacterStream'");
    }

    @Override
    public void updateNCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNCharacterStream'");
    }

    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAsciiStream'");
    }

    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBinaryStream'");
    }

    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCharacterStream'");
    }

    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAsciiStream'");
    }

    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBinaryStream'");
    }

    @Override
    public void updateCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCharacterStream'");
    }

    @Override
    public void updateBlob(final int columnIndex, final InputStream inputStream) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBlob'");
    }

    @Override
    public void updateBlob(final String columnLabel, final InputStream inputStream) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBlob'");
    }

    @Override
    public void updateClob(final int columnIndex, final Reader reader) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateClob'");
    }

    @Override
    public void updateClob(final String columnLabel, final Reader reader) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateClob'");
    }

    @Override
    public void updateNClob(final int columnIndex, final Reader reader) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNClob'");
    }

    @Override
    public void updateNClob(final String columnLabel, final Reader reader) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateNClob'");
    }

    @Override
    public <T> T getObject(final int columnIndex, final Class<T> type) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getObject'");
    }

    @Override
    public <T> T getObject(final String columnLabel, final Class<T> type) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getObject'");
    }
}
