package org.itsallcode.jdbc;

import org.itsallcode.jdbc.resultset.ValueExtractorFactory;
import org.itsallcode.jdbc.update.ParameterMapper;

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
