package ca.turbobutterfly.wachadoin.data;

import java.util.Date;

import ca.turbobutterfly.data.IDataTable;

public interface IDataProvider
{
    Date GetLastEndTime();

    String GetLastLogText();

    String[] GetRecentLogText(boolean forceRead);

    boolean SaveLogEntry(Date startTime, Date endTime, String logText);

    IDataTable GetLogEntries();
}
