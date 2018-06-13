package ca.turbobutterfly.wachadoin.core.data;

import java.util.Date;

import ca.turbobutterfly.core.data.IDataTable;

public interface IDataProvider
{
    Date GetLastEndTime();

    String GetLastLogText();

    String[] GetRecentLogText(boolean forceRead);

    boolean SaveLogEntry(Date startTime, Date endTime, String logText);

    IDataTable GetLogEntries(Date rangeStart, Date rangeEnd, int snapTime);
}
