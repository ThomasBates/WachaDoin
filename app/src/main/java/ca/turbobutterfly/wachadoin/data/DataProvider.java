package ca.turbobutterfly.wachadoin.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ca.turbobutterfly.data.IDataRow;
import ca.turbobutterfly.data.IDataTable;
import ca.turbobutterfly.data.DataTable;

public class DataProvider implements IDataProvider
{
    //  Fields -------------------------------------------------------------------------------------

    private final IDataAccess _dataAccess;

    private Date _lastEndDate;
    private String _lastLogText;
    private String[] _recentLogText;
    private IDataTable _logEntries;

    private final SimpleDateFormat _iso8601DateTimeFormat
            = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());

    private final SimpleDateFormat _logDateFormat
            = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private final SimpleDateFormat _logTimeFormat
            = new SimpleDateFormat("HH:mm", Locale.getDefault());

    private final SimpleDateFormat _logSpanFormat
            = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    private final long _msPerDay = 1000 * 60 * 60 * 24;

    //  Constructors -------------------------------------------------------------------------------

    public DataProvider(IDataAccess dataAccess)
    {
        _dataAccess = dataAccess;

        _logSpanFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    //  Methods ------------------------------------------------------------------------------------

    @Override
    public Date GetLastEndTime()
    {
        if (_lastEndDate == null)
        {
            GetRecentLogText(false);
        }
        return _lastEndDate;
    }

    @Override
    public String GetLastLogText()
    {
        if (_lastLogText == null)
        {
            GetRecentLogText(false);
        }
        return _lastLogText;
    }

    @Override
    public String[] GetRecentLogText(boolean forceRead)
    {
        if ((_recentLogText == null) || forceRead)
        {
            List<String> list = new ArrayList<>();

            Cursor cursor = _dataAccess.GetLogSummary();

            if (cursor != null)
            {
                _lastEndDate = new Date();
                _lastLogText = "";

                if (cursor.moveToFirst())
                {
                    int maxEndDateIndex = cursor.getColumnIndex("MaxEndTime");
                    int logTextIndex = cursor.getColumnIndex("LogText");

                    _lastEndDate = ISO8601ToDate(cursor.getString(maxEndDateIndex), new Date());
                    _lastLogText = cursor.getString(logTextIndex);

                    do
                    {
                        list.add(cursor.getString(logTextIndex));
                    } while (cursor.moveToNext() && (list.size() < 20));
                }
                cursor.close();
            }

            _recentLogText = list.toArray(new String[list.size()]);
        }

        return _recentLogText;
    }

    @Override
    public boolean SaveLogEntry(Date startTime, Date endTime, String logText)
    {
        ContentValues values = new ContentValues();
        values.put("StartTime", _iso8601DateTimeFormat.format(startTime));
        values.put("EndTime", _iso8601DateTimeFormat.format(endTime));
        values.put("LogText", logText);

        long id = _dataAccess.SaveLog(values);

        if (id == 0)
        {
            return false;
        }

        _lastEndDate = null;
        _lastLogText = null;
        _recentLogText = null;
        _logEntries = null;

        return true;
    }

    @Override
    public IDataTable GetLogEntries()
    {
        if (_logEntries == null)
        {
            _logEntries = new DataTable();
            _logEntries.AddColumns(new String[]{
                    "Date",
                    "StartTime",
                    "EndTime",
                    "TotalTime",
                    "LogText"
            });

            Cursor cursor = _dataAccess.GetLogDetails();

            if (cursor != null)
            {
                if (cursor.moveToFirst())
                {
                    int startTimeIndex = cursor.getColumnIndex("StartTime");
                    int endTimeIndex = cursor.getColumnIndex("EndTime");
                    int logTextIndex = cursor.getColumnIndex("LogText");

                    Date lastStartTime = null;
                    Date lastEndTime = null;
                    String lastLogText = null;

                    do
                    {
                        Date startTime = ISO8601ToDate(cursor.getString(startTimeIndex), new Date());
                        Date endTime = ISO8601ToDate(cursor.getString(endTimeIndex), new Date());
                        String logText = cursor.getString(logTextIndex);

                        if (lastStartTime != null)
                        {
                            AddLogEntryWithSplit(lastStartTime, lastEndTime, lastLogText);
                        }

                        lastStartTime = startTime;
                        lastEndTime = endTime;
                        lastLogText = logText;
                    }
                    while (cursor.moveToNext() && (_logEntries.RowCount() < 100));

                    if (lastStartTime != null)
                    {
                        AddLogEntryWithSplit(lastStartTime, lastEndTime, lastLogText);
                    }
                }
                cursor.close();
            }
        }

        return _logEntries;
    }

    private void AddLogEntryWithSplit(Date lastStartTime, Date lastEndTime, String lastLogText)
    {
        //  If it starts on one day and ends on another day,
        //  make multiple entries.
        int offset = TimeZone.getDefault().getOffset(lastStartTime.getTime());
        long startDay = (lastStartTime.getTime() + offset) / _msPerDay;
        long endDay = (lastEndTime.getTime() + offset - 1) / _msPerDay;
        while (startDay < endDay)
        {
            Date tempStartTime = new Date((endDay * _msPerDay) - offset);

            AddLogEntry(tempStartTime, lastEndTime, lastLogText);

            lastEndTime = tempStartTime;
            endDay = (lastEndTime.getTime() + offset - 1) / _msPerDay;
        }

        AddLogEntry(lastStartTime, lastEndTime, lastLogText);
    }

    private void AddLogEntry(Date startTime, Date endTime, String logText)
    {
        Date midTime = new Date((endTime.getTime() + startTime.getTime()) / 2);

        //  use only the minute portion for calculating time span.
        long startTime_ms = (startTime.getTime() / 60000) * 60000;
        long endTime_ms = (endTime.getTime() / 60000) * 60000;
        Date totalTime = new Date(endTime_ms - startTime_ms);

        String dateValue = _logDateFormat.format(midTime);
        String startTimeValue = _logTimeFormat.format(startTime);
        String endTimeValue = _logTimeFormat.format(endTime);
        String totalTimeValue = _logSpanFormat.format(totalTime);

        if (TextUtils.equals(endTimeValue, "00:00"))
        {
            endTimeValue = "24:00";
        }

        if (TextUtils.equals(totalTimeValue, "00:00") &&
                (totalTime.getTime() > (_msPerDay / 2)))
        {
            totalTimeValue = "24:00";
        }

        IDataRow row = _logEntries.NewRow();
        row.Value(0, dateValue);
        row.Value(1, startTimeValue);
        row.Value(2, endTimeValue);
        row.Value(3, totalTimeValue);
        row.Value(4, logText);
        _logEntries.AddRow(row);
    }

    private Date ISO8601ToDate(String iso8601Date, Date defaultValue)
    {
        try
        {
            return _iso8601DateTimeFormat.parse(iso8601Date);
        }
        catch (ParseException e)
        {
            return defaultValue;
        }
    }
}
