package ca.turbobutterfly.wachadoin.core.viewmodels;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import ca.turbobutterfly.core.mvvm.Command;
import ca.turbobutterfly.core.mvvm.CommandListener;
import ca.turbobutterfly.core.mvvm.ICommand;
import ca.turbobutterfly.core.utils.DateUtils;
import ca.turbobutterfly.core.utils.TextUtils;
import ca.turbobutterfly.core.mvvm.ViewModel;

import ca.turbobutterfly.wachadoin.core.data.IDataProvider;
import ca.turbobutterfly.wachadoin.core.logfile.ILogFileBuilder;
import ca.turbobutterfly.wachadoin.core.logfile.ILogFileDelivery;
import ca.turbobutterfly.wachadoin.core.logfile.ILogFileDeliveryFactory;
import ca.turbobutterfly.wachadoin.core.logfile.LogFileCSVBuilder;
import ca.turbobutterfly.wachadoin.core.logfile.LogFileTextBuilder;
import ca.turbobutterfly.wachadoin.core.options.IMainOptions;

public class ExportPageViewModel extends ViewModel
{
    //  Variables ----------------------------------------------------------------------------------

    //  Injected dependencies
    private IDataProvider _dataProvider;
    private IMainOptions _mainOptions;
    private ILogFileDeliveryFactory _logFileDeliveryFactory;

    //  Property backers
    private Date _startTime;
    private String _startDateText;
    private Date _endTime;
    private String _endDateText;

    private Boolean _use_reporting_period;
    private Integer _reporting_period;
    private Date _reporting_period_start;
    private Integer _days_per_page;
    private boolean _groupByDate;
    private String _logOrder;
    private int _roundTime;
    private String _fileFormat;
    private String _fileDelivery;
    private String _emailAddress;
    private int _statusColor;
    private String _statusText;

    private ICommand _exportCommand = new Command(new CommandListener()
    {
        @Override
        public void Execute(Object parameters)
        {
            DoExport();
        }
    });

    //  Constants
//    private final long _msPerHour = 1000 * 60 * 60;
//    private final long _msPerDay = _msPerHour * 24;

    //  Constructors -------------------------------------------------------------------------------

    public ExportPageViewModel(
            IDataProvider dataProvider,
            IMainOptions mainOptions,
            ILogFileDeliveryFactory logFileDeliveryFactory)
    {
        _dataProvider = dataProvider;
        _mainOptions = mainOptions;
        _logFileDeliveryFactory = logFileDeliveryFactory;

        _use_reporting_period = _mainOptions.Display().use_reporting_period().Value();
        _reporting_period = _mainOptions.Display().reporting_period().Value();
        _reporting_period_start = _mainOptions.Display().reporting_period_start().Value();
        _days_per_page = _mainOptions.Display().days_per_page().Value();
        _groupByDate = _mainOptions.Display().group_by_date().Value();
        _logOrder = _mainOptions.Display().order().Value();
        _roundTime = _mainOptions.Display().round().Value();

        _fileFormat = _mainOptions.Export().format().Value();
        _fileDelivery = _mainOptions.Export().delivery().Value();
        _emailAddress = _mainOptions.Export().email().Value();

        SetInitialDataRange();
    }

    private void SetInitialDataRange()
    {
        if (_use_reporting_period)
        {
            Date now = new Date();
            _startTime = _reporting_period_start;
            while (_startTime.before(now))
            {
                _startTime = IncrementDate(_startTime);
            }
            while (_startTime.after(now))
            {
                _startTime = DecrementDate(_startTime);
            }
            //  Once more so we're exporting the most
            //  recently completed reporting period.
            _startTime = DecrementDate(_startTime);
            _endTime = IncrementDate(_startTime);

            //  Update reporting_period_start if we're in the next reporting period.
            //  It makes no difference to program operation, but it will show the new date on the Settings screen.
            if (!DateUtils.equals(_startTime, _reporting_period_start))
            {
                _reporting_period_start = _startTime;
                _mainOptions.Display().reporting_period_start().Value(_reporting_period_start);
            }
        }
        else
        {
            //  Get date without time.
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            //  We need the end of today which is the start of tomorrow.
            calendar = new GregorianCalendar(year, month, day);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            _endTime = calendar.getTime();
            _startTime = DecrementDate(_endTime);
        }

        //  Turn _endTime back one day for display purposes.
        _endTime = DecrementOneDay(_endTime);

        _startDateText = DateUtils.LongDate(_startTime);
        _endDateText = DateUtils.LongDate(_endTime);
    }

    //  Properties ---------------------------------------------------------------------------------

    public Date StartTime()
    {
        return _startTime;
    }

    public void StartTime(Date startTime)
    {
        //  Round to nearest hour.
        //startTime = new Date((startTime.getTime() / _msPerHour) * _msPerHour);

        if (DateUtils.equals(startTime, _startTime))
        {
            return;
        }
        if (startTime.after(_endTime))
        {
            return;
        }
        _startTime = startTime;
        NotifyPropertyChanged("StartTime");

        StartDateText(DateUtils.LongDate(_startTime));
    }

    public String StartDateText()
    {
        return _startDateText;
    }

    public void StartDateText(String startDateText)
    {
        if (TextUtils.equals(startDateText, _startDateText))
        {
            return;
        }
        _startDateText = startDateText;
        NotifyPropertyChanged("StartDateText");
    }

    public Date EndTime()
    {
        return _endTime;
    }

    public void EndTime(Date endTime)
    {
        //  Round to nearest hour.
        //endTime = new Date((endTime.getTime() / _msPerHour) * _msPerHour);

        if (DateUtils.equals(endTime, _endTime))
        {
            return;
        }
        if (endTime.before(_startTime))
        {
            return;
        }
        _endTime = endTime;
        NotifyPropertyChanged("EndTime");

        EndDateText(DateUtils.LongDate(_endTime));
    }

    public String EndDateText()
    {
        return _endDateText;
    }

    public void EndDateText(String endDateText)
    {
        if (TextUtils.equals(endDateText, _endDateText))
        {
            return;
        }
        _endDateText = endDateText;
        NotifyPropertyChanged("EndDateText");
    }

    public boolean GroupByDate()
    {
        return _groupByDate;
    }

    public void GroupByDate(boolean groupByDate)
    {
        if (groupByDate == _groupByDate)
        {
            return;
        }
        _groupByDate = groupByDate;
        NotifyPropertyChanged("GroupByDate");
    }

    public String LogOrder()
    {
        return _logOrder;
    }

    public void LogOrder(String logOrder)
    {
        if (TextUtils.equals(logOrder, _logOrder))
        {
            return;
        }
        _logOrder = logOrder;
        NotifyPropertyChanged("LogOrder");
    }

    public Integer RoundTime()
    {
        return _roundTime;
    }

    public void RoundTime(Integer roundTime)
    {
        if (roundTime == _roundTime)
        {
            return;
        }
        _roundTime = roundTime;
        NotifyPropertyChanged("RoundTime");
    }

    public String FileFormat()
    {
        return _fileFormat;
    }

    public void FileFormat(String fileFormat)
    {
        if (TextUtils.equals(fileFormat, _fileFormat))
        {
            return;
        }
        _fileFormat = fileFormat;
        NotifyPropertyChanged("FileFormat");
    }

    public String FileDelivery()
    {
        return _fileDelivery;
    }

    public void FileDelivery(String fileDelivery)
    {
        if (TextUtils.equals(fileDelivery, _fileDelivery))
        {
            return;
        }
        _fileDelivery = fileDelivery;
        NotifyPropertyChanged("FileDelivery");
    }

    public String EmailAddress()
    {
        return _emailAddress;
    }

    public void EmailAddress(String emailAddress)
    {
        if (TextUtils.equals(emailAddress, _emailAddress))
        {
            return;
        }
        _emailAddress = emailAddress;
        NotifyPropertyChanged("EmailAddress");
    }

    public String StatusText()
    {
        return _statusText;
    }

    public void StatusText(String statusText)
    {
        if (TextUtils.equals(statusText, _statusText))
        {
            return;
        }
        _statusText = statusText;
        NotifyPropertyChanged("StatusText");
    }

    public int StatusColor()
    {
        return _statusColor;
    }

    public void StatusColor(int statusColor)
    {
        if (statusColor == _statusColor)
        {
            return;
        }
        _statusColor = statusColor;
        NotifyPropertyChanged("StatusColor");
    }

    public ICommand ExportCommand()
    {
        return _exportCommand;
    }

    //  Methods ------------------------------------------------------------------------------------

    private void DoExport()
    {
        StatusColor(0xFF0000FF); // Blue
        StatusText("Export in Progress.");

        String logFile = CreateLogFile();
        boolean success = DeliverLogFile(logFile);

        if (success)
        {
            StatusColor(0xFF008800); // Green
            StatusText("Export Successful!");
        }
        else
        {
            StatusColor(0xFFFF0000); // Red
            StatusText("Export Failed!");
        }
    }

    private String CreateLogFile()
    {
        ILogFileBuilder builder;
        switch (_fileFormat)
        {
            case "Text":
                builder = new LogFileTextBuilder(_dataProvider);
                break;
            case "CSV":
                builder = new LogFileCSVBuilder(_dataProvider);
                break;
            default:
                return "";
        }

        //  _endTime is the beginning of the last day. we need to send the end of the last day.
        Date endTime = IncrementOneDay(_endTime);

        return builder.BuildLogFile(_startTime, endTime, _groupByDate, _logOrder, _roundTime);
    }

    private boolean DeliverLogFile(String logFile)
    {
        ILogFileDelivery delivery = _logFileDeliveryFactory.GetLogFileDelivery(_fileDelivery);

        if (TextUtils.equals(_fileDelivery, "Email"))
        {
            delivery.Extra("EmailAddress", _emailAddress);
        }

        switch (_fileFormat)
        {
            case "Text":
                delivery.Extra("FileExtension", ".txt");
                break;
            case "CSV":
                delivery.Extra("FileExtension", ".csv");
                break;
        }

        return delivery.DeliverLogFile(logFile);
    }

    private Date IncrementDate(Date date)
    {
        int days = _days_per_page;
        if (_use_reporting_period)
        {
            days = _reporting_period;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (days)
        {
            case 1:
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                break;
            case 7:
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case 14:
                calendar.add(Calendar.WEEK_OF_YEAR, 2);
                break;
            case 30:
                calendar.add(Calendar.MONTH, 1);
                break;
        }
        return calendar.getTime();
    }

    private Date DecrementDate(Date date)
    {
        int days = _days_per_page;
        if (_use_reporting_period)
        {
            days = _reporting_period;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        switch (days)
        {
            case 1:
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                break;
            case 7:
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                break;
            case 14:
                calendar.add(Calendar.WEEK_OF_YEAR, -2);
                break;
            case 30:
                calendar.add(Calendar.MONTH, -1);
                break;
        }
        return calendar.getTime();
    }

    private Date IncrementOneDay(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    private Date DecrementOneDay(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }
}
