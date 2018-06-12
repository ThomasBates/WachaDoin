package ca.turbobutterfly.wachadoin.core.data;

//import android.content.ContentValues;


import java.util.Date;

import ca.turbobutterfly.core.data.IDataReader;

public interface IDataAccess
{
    //long SaveLog(ContentValues values);
    long SaveLogEntry(IDataLogEntry logEntry);

    IDataReader GetLogSummary();

    IDataReader GetLogDetails(Date rangeStart, Date rangeEnd);
}
