package ca.turbobutterfly.wachadoin.core.data;

import java.util.Date;

import ca.turbobutterfly.core.data.IDataColumn;
import ca.turbobutterfly.core.data.IDataRow;
import ca.turbobutterfly.core.data.IDataTable;

public interface IDataProvider
{
    Date GetLastEndTime();
    String GetLastLogText();

    boolean SaveLogEntry(Date startTime, Date endTime, String logText);
    boolean UpdateLogEntry(Date startTime, Date endTime, String logText, String fieldName);

    String[] GetRecentLogText(boolean forceRead);
    IDataTable GetLogEntries(Date rangeStart, Date rangeEnd, boolean longDate, int roundTime);
}
