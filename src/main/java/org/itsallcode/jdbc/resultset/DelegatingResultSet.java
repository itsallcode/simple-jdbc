package org.itsallcode.jdbc.resultset;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

@SuppressWarnings("java:S1448") // Long file required for implementing ResultSet
class DelegatingResultSet implements ResultSet {

    private final ResultSet delegate;

    DelegatingResultSet(final ResultSet delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        return delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return delegate.isWrapperFor(iface);
    }

    @Override
    public boolean next() throws SQLException {
        return delegate.next();
    }

    @Override
    public void close() throws SQLException {
        delegate.close();
    }

    @Override
    public boolean wasNull() throws SQLException {
        return delegate.wasNull();
    }

    @Override
    public String getString(final int columnIndex) throws SQLException {
        return delegate.getString(columnIndex);
    }

    @Override
    public boolean getBoolean(final int columnIndex) throws SQLException {
        return delegate.getBoolean(columnIndex);
    }

    @Override
    public byte getByte(final int columnIndex) throws SQLException {
        return delegate.getByte(columnIndex);
    }

    @Override
    public short getShort(final int columnIndex) throws SQLException {
        return delegate.getShort(columnIndex);
    }

    @Override
    public int getInt(final int columnIndex) throws SQLException {
        return delegate.getInt(columnIndex);
    }

    @Override
    public long getLong(final int columnIndex) throws SQLException {
        return delegate.getLong(columnIndex);
    }

    @Override
    public float getFloat(final int columnIndex) throws SQLException {
        return delegate.getFloat(columnIndex);
    }

    @Override
    public double getDouble(final int columnIndex) throws SQLException {
        return delegate.getDouble(columnIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @deprecated Use {@code getBigDecimal(int columnIndex)} or
     *             {@code getBigDecimal(String columnLabel)}
     */
    @Override
    @Deprecated(since = "0.7.1")
    public BigDecimal getBigDecimal(final int columnIndex, final int scale) throws SQLException {
        return delegate.getBigDecimal(columnIndex, scale);
    }

    @Override
    public byte[] getBytes(final int columnIndex) throws SQLException {
        return delegate.getBytes(columnIndex);
    }

    @Override
    public Date getDate(final int columnIndex) throws SQLException {
        return delegate.getDate(columnIndex);
    }

    @Override
    public Time getTime(final int columnIndex) throws SQLException {
        return delegate.getTime(columnIndex);
    }

    @Override
    public Timestamp getTimestamp(final int columnIndex) throws SQLException {
        return delegate.getTimestamp(columnIndex);
    }

    @Override
    public InputStream getAsciiStream(final int columnIndex) throws SQLException {
        return delegate.getAsciiStream(columnIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @deprecated use {@code getCharacterStream} in place of
     *             {@code getUnicodeStream}
     */
    @Override
    @Deprecated(since = "0.7.1")
    public InputStream getUnicodeStream(final int columnIndex) throws SQLException {
        return delegate.getUnicodeStream(columnIndex);
    }

    @Override
    public InputStream getBinaryStream(final int columnIndex) throws SQLException {
        return delegate.getBinaryStream(columnIndex);
    }

    @Override
    public String getString(final String columnLabel) throws SQLException {
        return delegate.getString(columnLabel);
    }

    @Override
    public boolean getBoolean(final String columnLabel) throws SQLException {
        return delegate.getBoolean(columnLabel);
    }

    @Override
    public byte getByte(final String columnLabel) throws SQLException {
        return delegate.getByte(columnLabel);
    }

    @Override
    public short getShort(final String columnLabel) throws SQLException {
        return delegate.getShort(columnLabel);
    }

    @Override
    public int getInt(final String columnLabel) throws SQLException {
        return delegate.getInt(columnLabel);
    }

    @Override
    public long getLong(final String columnLabel) throws SQLException {
        return delegate.getLong(columnLabel);
    }

    @Override
    public float getFloat(final String columnLabel) throws SQLException {
        return delegate.getFloat(columnLabel);
    }

    @Override
    public double getDouble(final String columnLabel) throws SQLException {
        return delegate.getDouble(columnLabel);
    }

    /**
     * {@inheritDoc}
     * 
     * @deprecated Use {@code getBigDecimal(int columnIndex)} or
     *             {@code getBigDecimal(String columnLabel)}
     */
    @Override
    @Deprecated(since = "0.7.1")
    public BigDecimal getBigDecimal(final String columnLabel, final int scale) throws SQLException {
        return delegate.getBigDecimal(columnLabel, scale);
    }

    @Override
    public byte[] getBytes(final String columnLabel) throws SQLException {
        return delegate.getBytes(columnLabel);
    }

    @Override
    public Date getDate(final String columnLabel) throws SQLException {
        return delegate.getDate(columnLabel);
    }

    @Override
    public Time getTime(final String columnLabel) throws SQLException {
        return delegate.getTime(columnLabel);
    }

    @Override
    public Timestamp getTimestamp(final String columnLabel) throws SQLException {
        return delegate.getTimestamp(columnLabel);
    }

    @Override
    public InputStream getAsciiStream(final String columnLabel) throws SQLException {
        return delegate.getAsciiStream(columnLabel);
    }

    /**
     * {@inheritDoc}
     * 
     * @deprecated use {@code getCharacterStream} instead
     */
    @Override
    @Deprecated(since = "0.7.1")
    public InputStream getUnicodeStream(final String columnLabel) throws SQLException {
        return delegate.getUnicodeStream(columnLabel);
    }

    @Override
    public InputStream getBinaryStream(final String columnLabel) throws SQLException {
        return delegate.getBinaryStream(columnLabel);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return delegate.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        delegate.clearWarnings();
    }

    @Override
    public String getCursorName() throws SQLException {
        return delegate.getCursorName();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return delegate.getMetaData();
    }

    @Override
    public Object getObject(final int columnIndex) throws SQLException {
        return delegate.getObject(columnIndex);
    }

    @Override
    public Object getObject(final String columnLabel) throws SQLException {
        return delegate.getObject(columnLabel);
    }

    @Override
    public int findColumn(final String columnLabel) throws SQLException {
        return delegate.findColumn(columnLabel);
    }

    @Override
    public Reader getCharacterStream(final int columnIndex) throws SQLException {
        return delegate.getCharacterStream(columnIndex);
    }

    @Override
    public Reader getCharacterStream(final String columnLabel) throws SQLException {
        return delegate.getCharacterStream(columnLabel);
    }

    @Override
    public BigDecimal getBigDecimal(final int columnIndex) throws SQLException {
        return delegate.getBigDecimal(columnIndex);
    }

    @Override
    public BigDecimal getBigDecimal(final String columnLabel) throws SQLException {
        return delegate.getBigDecimal(columnLabel);
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        return delegate.isBeforeFirst();
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        return delegate.isAfterLast();
    }

    @Override
    public boolean isFirst() throws SQLException {
        return delegate.isFirst();
    }

    @Override
    @SuppressWarnings("java:S2232") // Need to call isLast() for delegation
    public boolean isLast() throws SQLException {
        return delegate.isLast();
    }

    @Override
    public void beforeFirst() throws SQLException {
        delegate.beforeFirst();
    }

    @Override
    public void afterLast() throws SQLException {
        delegate.afterLast();
    }

    @Override
    public boolean first() throws SQLException {
        return delegate.first();
    }

    @Override
    public boolean last() throws SQLException {
        return delegate.last();
    }

    @Override
    public int getRow() throws SQLException {
        return delegate.getRow();
    }

    @Override
    public boolean absolute(final int row) throws SQLException {
        return delegate.absolute(row);
    }

    @Override
    public boolean relative(final int rows) throws SQLException {
        return delegate.relative(rows);
    }

    @Override
    public boolean previous() throws SQLException {
        return delegate.previous();
    }

    @Override
    public void setFetchDirection(final int direction) throws SQLException {
        delegate.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return delegate.getFetchDirection();
    }

    @Override
    public void setFetchSize(final int rows) throws SQLException {
        delegate.setFetchSize(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return delegate.getFetchSize();
    }

    @Override
    public int getType() throws SQLException {
        return delegate.getType();
    }

    @Override
    public int getConcurrency() throws SQLException {
        return delegate.getConcurrency();
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        return delegate.rowUpdated();
    }

    @Override
    public boolean rowInserted() throws SQLException {
        return delegate.rowInserted();
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        return delegate.rowDeleted();
    }

    @Override
    public void updateNull(final int columnIndex) throws SQLException {
        delegate.updateNull(columnIndex);
    }

    @Override
    public void updateBoolean(final int columnIndex, final boolean x) throws SQLException {
        delegate.updateBoolean(columnIndex, x);
    }

    @Override
    public void updateByte(final int columnIndex, final byte x) throws SQLException {
        delegate.updateByte(columnIndex, x);
    }

    @Override
    public void updateShort(final int columnIndex, final short x) throws SQLException {
        delegate.updateShort(columnIndex, x);
    }

    @Override
    public void updateInt(final int columnIndex, final int x) throws SQLException {
        delegate.updateInt(columnIndex, x);
    }

    @Override
    public void updateLong(final int columnIndex, final long x) throws SQLException {
        delegate.updateLong(columnIndex, x);
    }

    @Override
    public void updateFloat(final int columnIndex, final float x) throws SQLException {
        delegate.updateFloat(columnIndex, x);
    }

    @Override
    public void updateDouble(final int columnIndex, final double x) throws SQLException {
        delegate.updateDouble(columnIndex, x);
    }

    @Override
    public void updateBigDecimal(final int columnIndex, final BigDecimal x) throws SQLException {
        delegate.updateBigDecimal(columnIndex, x);
    }

    @Override
    public void updateString(final int columnIndex, final String x) throws SQLException {
        delegate.updateString(columnIndex, x);
    }

    @Override
    public void updateBytes(final int columnIndex, final byte[] x) throws SQLException {
        delegate.updateBytes(columnIndex, x);
    }

    @Override
    public void updateDate(final int columnIndex, final Date x) throws SQLException {
        delegate.updateDate(columnIndex, x);
    }

    @Override
    public void updateTime(final int columnIndex, final Time x) throws SQLException {
        delegate.updateTime(columnIndex, x);
    }

    @Override
    public void updateTimestamp(final int columnIndex, final Timestamp x) throws SQLException {
        delegate.updateTimestamp(columnIndex, x);
    }

    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        delegate.updateAsciiStream(columnIndex, x, length);
    }

    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x, final int length) throws SQLException {
        delegate.updateBinaryStream(columnIndex, x, length);
    }

    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x, final int length) throws SQLException {
        delegate.updateCharacterStream(columnIndex, x, length);
    }

    @Override
    public void updateObject(final int columnIndex, final Object x, final int scaleOrLength) throws SQLException {
        delegate.updateObject(columnIndex, x, scaleOrLength);
    }

    @Override
    public void updateObject(final int columnIndex, final Object x) throws SQLException {
        delegate.updateObject(columnIndex, x);
    }

    @Override
    public void updateNull(final String columnLabel) throws SQLException {
        delegate.updateNull(columnLabel);
    }

    @Override
    public void updateBoolean(final String columnLabel, final boolean x) throws SQLException {
        delegate.updateBoolean(columnLabel, x);
    }

    @Override
    public void updateByte(final String columnLabel, final byte x) throws SQLException {
        delegate.updateByte(columnLabel, x);
    }

    @Override
    public void updateShort(final String columnLabel, final short x) throws SQLException {
        delegate.updateShort(columnLabel, x);
    }

    @Override
    public void updateInt(final String columnLabel, final int x) throws SQLException {
        delegate.updateInt(columnLabel, x);
    }

    @Override
    public void updateLong(final String columnLabel, final long x) throws SQLException {
        delegate.updateLong(columnLabel, x);
    }

    @Override
    public void updateFloat(final String columnLabel, final float x) throws SQLException {
        delegate.updateFloat(columnLabel, x);
    }

    @Override
    public void updateDouble(final String columnLabel, final double x) throws SQLException {
        delegate.updateDouble(columnLabel, x);
    }

    @Override
    public void updateBigDecimal(final String columnLabel, final BigDecimal x) throws SQLException {
        delegate.updateBigDecimal(columnLabel, x);
    }

    @Override
    public void updateString(final String columnLabel, final String x) throws SQLException {
        delegate.updateString(columnLabel, x);
    }

    @Override
    public void updateBytes(final String columnLabel, final byte[] x) throws SQLException {
        delegate.updateBytes(columnLabel, x);
    }

    @Override
    public void updateDate(final String columnLabel, final Date x) throws SQLException {
        delegate.updateDate(columnLabel, x);
    }

    @Override
    public void updateTime(final String columnLabel, final Time x) throws SQLException {
        delegate.updateTime(columnLabel, x);
    }

    @Override
    public void updateTimestamp(final String columnLabel, final Timestamp x) throws SQLException {
        delegate.updateTimestamp(columnLabel, x);
    }

    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x, final int length) throws SQLException {
        delegate.updateAsciiStream(columnLabel, x, length);
    }

    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x, final int length)
            throws SQLException {
        delegate.updateBinaryStream(columnLabel, x, length);
    }

    @Override
    public void updateCharacterStream(final String columnLabel, final Reader reader, final int length)
            throws SQLException {
        delegate.updateCharacterStream(columnLabel, reader, length);
    }

    @Override
    public void updateObject(final String columnLabel, final Object x, final int scaleOrLength) throws SQLException {
        delegate.updateObject(columnLabel, x, scaleOrLength);
    }

    @Override
    public void updateObject(final String columnLabel, final Object x) throws SQLException {
        delegate.updateObject(columnLabel, x);
    }

    @Override
    public void insertRow() throws SQLException {
        delegate.insertRow();
    }

    @Override
    public void updateRow() throws SQLException {
        delegate.updateRow();
    }

    @Override
    public void deleteRow() throws SQLException {
        delegate.deleteRow();
    }

    @Override
    public void refreshRow() throws SQLException {
        delegate.refreshRow();
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        delegate.cancelRowUpdates();
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        delegate.moveToInsertRow();
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        delegate.moveToCurrentRow();
    }

    @Override
    public Statement getStatement() throws SQLException {
        return delegate.getStatement();
    }

    @Override
    public Object getObject(final int columnIndex, final Map<String, Class<?>> map) throws SQLException {
        return delegate.getObject(columnIndex, map);
    }

    @Override
    public Ref getRef(final int columnIndex) throws SQLException {
        return delegate.getRef(columnIndex);
    }

    @Override
    public Blob getBlob(final int columnIndex) throws SQLException {
        return delegate.getBlob(columnIndex);
    }

    @Override
    public Clob getClob(final int columnIndex) throws SQLException {
        return delegate.getClob(columnIndex);
    }

    @Override
    public Array getArray(final int columnIndex) throws SQLException {
        return delegate.getArray(columnIndex);
    }

    @Override
    public Object getObject(final String columnLabel, final Map<String, Class<?>> map) throws SQLException {
        return delegate.getObject(columnLabel, map);
    }

    @Override
    public Ref getRef(final String columnLabel) throws SQLException {
        return delegate.getRef(columnLabel);
    }

    @Override
    public Blob getBlob(final String columnLabel) throws SQLException {
        return delegate.getBlob(columnLabel);
    }

    @Override
    public Clob getClob(final String columnLabel) throws SQLException {
        return delegate.getClob(columnLabel);
    }

    @Override
    public Array getArray(final String columnLabel) throws SQLException {
        return delegate.getArray(columnLabel);
    }

    @Override
    public Date getDate(final int columnIndex, final Calendar cal) throws SQLException {
        return delegate.getDate(columnIndex, cal);
    }

    @Override
    public Date getDate(final String columnLabel, final Calendar cal) throws SQLException {
        return delegate.getDate(columnLabel, cal);
    }

    @Override
    public Time getTime(final int columnIndex, final Calendar cal) throws SQLException {
        return delegate.getTime(columnIndex, cal);
    }

    @Override
    public Time getTime(final String columnLabel, final Calendar cal) throws SQLException {
        return delegate.getTime(columnLabel, cal);
    }

    @Override
    public Timestamp getTimestamp(final int columnIndex, final Calendar cal) throws SQLException {
        return delegate.getTimestamp(columnIndex, cal);
    }

    @Override
    public Timestamp getTimestamp(final String columnLabel, final Calendar cal) throws SQLException {
        return delegate.getTimestamp(columnLabel, cal);
    }

    @Override
    public URL getURL(final int columnIndex) throws SQLException {
        return delegate.getURL(columnIndex);
    }

    @Override
    public URL getURL(final String columnLabel) throws SQLException {
        return delegate.getURL(columnLabel);
    }

    @Override
    public void updateRef(final int columnIndex, final Ref x) throws SQLException {
        delegate.updateRef(columnIndex, x);
    }

    @Override
    public void updateRef(final String columnLabel, final Ref x) throws SQLException {
        delegate.updateRef(columnLabel, x);
    }

    @Override
    public void updateBlob(final int columnIndex, final Blob x) throws SQLException {
        delegate.updateBlob(columnIndex, x);
    }

    @Override
    public void updateBlob(final String columnLabel, final Blob x) throws SQLException {
        delegate.updateBlob(columnLabel, x);
    }

    @Override
    public void updateClob(final int columnIndex, final Clob x) throws SQLException {
        delegate.updateClob(columnIndex, x);
    }

    @Override
    public void updateClob(final String columnLabel, final Clob x) throws SQLException {
        delegate.updateClob(columnLabel, x);
    }

    @Override
    public void updateArray(final int columnIndex, final Array x) throws SQLException {
        delegate.updateArray(columnIndex, x);
    }

    @Override
    public void updateArray(final String columnLabel, final Array x) throws SQLException {
        delegate.updateArray(columnLabel, x);
    }

    @Override
    public RowId getRowId(final int columnIndex) throws SQLException {
        return delegate.getRowId(columnIndex);
    }

    @Override
    public RowId getRowId(final String columnLabel) throws SQLException {
        return delegate.getRowId(columnLabel);
    }

    @Override
    public void updateRowId(final int columnIndex, final RowId x) throws SQLException {
        delegate.updateRowId(columnIndex, x);
    }

    @Override
    public void updateRowId(final String columnLabel, final RowId x) throws SQLException {
        delegate.updateRowId(columnLabel, x);
    }

    @Override
    public int getHoldability() throws SQLException {
        return delegate.getHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return delegate.isClosed();
    }

    @Override
    public void updateNString(final int columnIndex, final String nString) throws SQLException {
        delegate.updateNString(columnIndex, nString);
    }

    @Override
    public void updateNString(final String columnLabel, final String nString) throws SQLException {
        delegate.updateNString(columnLabel, nString);
    }

    @Override
    public void updateNClob(final int columnIndex, final NClob nClob) throws SQLException {
        delegate.updateNClob(columnIndex, nClob);
    }

    @Override
    public void updateNClob(final String columnLabel, final NClob nClob) throws SQLException {
        delegate.updateNClob(columnLabel, nClob);
    }

    @Override
    public NClob getNClob(final int columnIndex) throws SQLException {
        return delegate.getNClob(columnIndex);
    }

    @Override
    public NClob getNClob(final String columnLabel) throws SQLException {
        return delegate.getNClob(columnLabel);
    }

    @Override
    public SQLXML getSQLXML(final int columnIndex) throws SQLException {
        return delegate.getSQLXML(columnIndex);
    }

    @Override
    public SQLXML getSQLXML(final String columnLabel) throws SQLException {
        return delegate.getSQLXML(columnLabel);
    }

    @Override
    public void updateSQLXML(final int columnIndex, final SQLXML xmlObject) throws SQLException {
        delegate.updateSQLXML(columnIndex, xmlObject);
    }

    @Override
    public void updateSQLXML(final String columnLabel, final SQLXML xmlObject) throws SQLException {
        delegate.updateSQLXML(columnLabel, xmlObject);
    }

    @Override
    public String getNString(final int columnIndex) throws SQLException {
        return delegate.getNString(columnIndex);
    }

    @Override
    public String getNString(final String columnLabel) throws SQLException {
        return delegate.getNString(columnLabel);
    }

    @Override
    public Reader getNCharacterStream(final int columnIndex) throws SQLException {
        return delegate.getNCharacterStream(columnIndex);
    }

    @Override
    public Reader getNCharacterStream(final String columnLabel) throws SQLException {
        return delegate.getNCharacterStream(columnLabel);
    }

    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
        delegate.updateNCharacterStream(columnIndex, x, length);
    }

    @Override
    public void updateNCharacterStream(final String columnLabel, final Reader reader, final long length)
            throws SQLException {
        delegate.updateNCharacterStream(columnLabel, reader, length);
    }

    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
        delegate.updateAsciiStream(columnIndex, x, length);
    }

    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x, final long length) throws SQLException {
        delegate.updateBinaryStream(columnIndex, x, length);
    }

    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x, final long length) throws SQLException {
        delegate.updateCharacterStream(columnIndex, x, length);
    }

    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x, final long length)
            throws SQLException {
        delegate.updateAsciiStream(columnLabel, x, length);
    }

    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x, final long length)
            throws SQLException {
        delegate.updateBinaryStream(columnLabel, x, length);
    }

    @Override
    public void updateCharacterStream(final String columnLabel, final Reader reader, final long length)
            throws SQLException {
        delegate.updateCharacterStream(columnLabel, reader, length);
    }

    @Override
    public void updateBlob(final int columnIndex, final InputStream inputStream, final long length)
            throws SQLException {
        delegate.updateBlob(columnIndex, inputStream, length);
    }

    @Override
    public void updateBlob(final String columnLabel, final InputStream inputStream, final long length)
            throws SQLException {
        delegate.updateBlob(columnLabel, inputStream, length);
    }

    @Override
    public void updateClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
        delegate.updateClob(columnIndex, reader, length);
    }

    @Override
    public void updateClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
        delegate.updateClob(columnLabel, reader, length);
    }

    @Override
    public void updateNClob(final int columnIndex, final Reader reader, final long length) throws SQLException {
        delegate.updateNClob(columnIndex, reader, length);
    }

    @Override
    public void updateNClob(final String columnLabel, final Reader reader, final long length) throws SQLException {
        delegate.updateNClob(columnLabel, reader, length);
    }

    @Override
    public void updateNCharacterStream(final int columnIndex, final Reader x) throws SQLException {
        delegate.updateNCharacterStream(columnIndex, x);
    }

    @Override
    public void updateNCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
        delegate.updateNCharacterStream(columnLabel, reader);
    }

    @Override
    public void updateAsciiStream(final int columnIndex, final InputStream x) throws SQLException {
        delegate.updateAsciiStream(columnIndex, x);
    }

    @Override
    public void updateBinaryStream(final int columnIndex, final InputStream x) throws SQLException {
        delegate.updateBinaryStream(columnIndex, x);
    }

    @Override
    public void updateCharacterStream(final int columnIndex, final Reader x) throws SQLException {
        delegate.updateCharacterStream(columnIndex, x);
    }

    @Override
    public void updateAsciiStream(final String columnLabel, final InputStream x) throws SQLException {
        delegate.updateAsciiStream(columnLabel, x);
    }

    @Override
    public void updateBinaryStream(final String columnLabel, final InputStream x) throws SQLException {
        delegate.updateBinaryStream(columnLabel, x);
    }

    @Override
    public void updateCharacterStream(final String columnLabel, final Reader reader) throws SQLException {
        delegate.updateCharacterStream(columnLabel, reader);
    }

    @Override
    public void updateBlob(final int columnIndex, final InputStream inputStream) throws SQLException {
        delegate.updateBlob(columnIndex, inputStream);
    }

    @Override
    public void updateBlob(final String columnLabel, final InputStream inputStream) throws SQLException {
        delegate.updateBlob(columnLabel, inputStream);
    }

    @Override
    public void updateClob(final int columnIndex, final Reader reader) throws SQLException {
        delegate.updateClob(columnIndex, reader);
    }

    @Override
    public void updateClob(final String columnLabel, final Reader reader) throws SQLException {
        delegate.updateClob(columnLabel, reader);
    }

    @Override
    public void updateNClob(final int columnIndex, final Reader reader) throws SQLException {
        delegate.updateNClob(columnIndex, reader);
    }

    @Override
    public void updateNClob(final String columnLabel, final Reader reader) throws SQLException {
        delegate.updateNClob(columnLabel, reader);
    }

    @Override
    public <T> T getObject(final int columnIndex, final Class<T> type) throws SQLException {
        return delegate.getObject(columnIndex, type);
    }

    @Override
    public <T> T getObject(final String columnLabel, final Class<T> type) throws SQLException {
        return delegate.getObject(columnLabel, type);
    }
}
