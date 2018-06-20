package ca.turbobutterfly.wachadoin.core.data;

import java.util.Date;

import ca.turbobutterfly.core.data.IDataReader;

public interface IDataAccess
{
    long SaveLogEntry(IDataLogEntry logEntry);
    int UpdateLogEntry(IDataLogEntry logEntry);

    IDataReader GetLogSummary();
    IDataReader GetLogDetails(Date rangeStart, Date rangeEnd);
}
