package ca.turbobutterfly.wachadoin.core.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ca.turbobutterfly.core.data.DataColumn;
import ca.turbobutterfly.core.data.IDataReader;
import ca.turbobutterfly.core.data.IDataRow;
import ca.turbobutterfly.core.data.IDataTable;
import ca.turbobutterfly.core.data.DataTable;
import ca.turbobutterfly.core.utils.DateUtils;
import ca.turbobutterfly.core.utils.TextUtils;
import ca.turbobutterfly.wachadoin.core.options.IMainOptions;

public class DataProvider implements IDataProvider
{
    //  Fields -------------------------------------------------------------------------------------

    private final IDataAccess _dataAccess;
    private final IMainOptions _mainOptions;

    private Date _lastEndDate;
    private String _lastLogText;
    private String[] _recentLogText;
    private IDataTable _logEntries;

    private final long _msPerDay = 1000 * 60 * 60 * 24;

    //  Constructors -------------------------------------------------------------------------------

    public DataProvider(
            IDataAccess dataAccess,
            IMainOptions mainOptions)
    {
        _dataAccess = dataAccess;
        _mainOptions = mainOptions;
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

            IDataReader reader = _dataAccess.GetLogSummary();

            _lastEndDate = new Date();
            _lastLogText = "";

            if (reader.Read())
            {
                _lastEndDate = DateUtils.ISO8601(reader.getString("MaxEndTime"), new Date());
                _lastLogText = reader.getString("LogText");

                list.add(reader.getString("LogText"));
            }

            while (reader.Read() && (list.size() < 20))
            {
                list.add(reader.getString("LogText"));
            }

            reader.Close();

            _recentLogText = list.toArray(new String[list.size()]);
        }

        return _recentLogText;
    }

    @Override
    public boolean SaveLogEntry(Date startTime, Date endTime, String logText)
    {
        _lastEndDate = null;
        _lastLogText = null;
        _recentLogText = null;
        _logEntries = null;

        //  If it starts on one day and ends on another day,
        //  make multiple entries.
        int offset_ms = TimeZone.getDefault().getOffset(startTime.getTime());
        long startDay_ms = (startTime.getTime() + offset_ms) / _msPerDay;
        long endDay_ms = (endTime.getTime() + offset_ms - 1) / _msPerDay;

        while (startDay_ms < endDay_ms)
        {
            Date tempEndTime = new Date((startDay_ms + 1) * _msPerDay - offset_ms);

            if (!SaveSameDayLogEntry(startTime, tempEndTime, logText))
            {
                return false;
            }

            startTime = tempEndTime;
            startDay_ms = (startTime.getTime() + offset_ms) / _msPerDay;
        }

        return SaveSameDayLogEntry(startTime, endTime, logText);
    }

    private boolean SaveSameDayLogEntry(Date startTime, Date endTime, String logText)
    {
        IDataLogEntry logEntry = new DataLogEntry();

        logEntry.StartTime(DateUtils.ISO8601(startTime));
        logEntry.EndTime(DateUtils.ISO8601(endTime));
        logEntry.LogText(logText);

        return (_dataAccess.SaveLogEntry(logEntry) != 0);
    }

    @Override
    public IDataTable GetLogEntries(Date rangeStart, Date rangeEnd)
    {
        _logEntries = new DataTable();
        _logEntries.AddColumn(new DataColumn("StartTime"));
        _logEntries.AddColumn(new DataColumn("EndTime"));
        _logEntries.AddColumn(new DataColumn("DisplayDate"));
        _logEntries.AddColumn(new DataColumn("DisplayStartTime"));
        _logEntries.AddColumn(new DataColumn("DisplayEndTime"));
        _logEntries.AddColumn(new DataColumn("DisplayTotalTime"));
        _logEntries.AddColumn(new DataColumn("LogText"));

        IDataReader reader = _dataAccess.GetLogDetails(rangeStart, rangeEnd);

        while (reader.Read())
        {
            String startTime = reader.getString("StartTime");
            String endTime = reader.getString("EndTime");
            String logText = reader.getString("LogText");

            AddLogEntryToTable(startTime, endTime, logText);
        }

        reader.Close();

        return _logEntries;
    }

    private void AddLogEntryToTable(String isoStartTime, String isoEndTime, String logText)
    {
        int snapTime = _mainOptions.Display().snap().Value() * 60000;
        int halfTime = snapTime / 2;

        Date startTime = DateUtils.ISO8601(isoStartTime, new Date(0));
        Date endTime = DateUtils.ISO8601(isoEndTime, new Date());

        //  use only the minute portion for calculating time span.
        long startTime_ms = ((startTime.getTime() + halfTime) / snapTime) * snapTime;
        long endTime_ms = ((endTime.getTime() + halfTime) / snapTime) * snapTime;
        startTime = new Date(startTime_ms);
        endTime = new Date(endTime_ms);
        Date totalTime = new Date(endTime_ms - startTime_ms);
        Date midTime = new Date(startTime_ms + (endTime_ms - startTime_ms) / 2);

        String displayDate = DateUtils.LongDate(midTime);
        String displayStartTime = DateUtils.ShortTime(startTime);
        String displayEndTime = DateUtils.ShortTime(endTime);
        String displayTotalTime = DateUtils.ShortTimeSpan(totalTime);

        if (TextUtils.equals(displayEndTime, "00:00"))
        {
            displayEndTime = "24:00";
        }

        if (TextUtils.equals(displayTotalTime, "00:00") &&
                (totalTime.getTime() > (_msPerDay / 2)))
        {
            displayTotalTime = "24:00";
        }

//        _logEntries.AddColumn(new DataColumn("StartTime"));
//        _logEntries.AddColumn(new DataColumn("EndTime"));
//        _logEntries.AddColumn(new DataColumn("DisplayDate"));
//        _logEntries.AddColumn(new DataColumn("DisplayStartTime"));
//        _logEntries.AddColumn(new DataColumn("DisplayEndTime"));
//        _logEntries.AddColumn(new DataColumn("DisplayTotalTime"));
//        _logEntries.AddColumn(new DataColumn("LogText"));


        IDataRow row = _logEntries.NewRow();
        row.Value("StartTime", isoStartTime);
        row.Value("EndTime", isoEndTime);
        row.Value("DisplayDate", displayDate);
        row.Value("DisplayStartTime", displayStartTime);
        row.Value("DisplayEndTime", displayEndTime);
        row.Value("DisplayTotalTime", displayTotalTime);
        row.Value("LogText", logText);
        _logEntries.AddRow(row);
    }
}
