package org.itsallcode.jdbc;

import org.itsallcode.jdbc.resultset.ValueExtractorFactory;

public class Context
{
    public ValueExtractorFactory getValueExtractorFactory()
    {
        return ValueExtractorFactory.create();
    }

    public ParameterMapper getParameterMapper()
    {
        return ParameterMapper.create();
    }
}
