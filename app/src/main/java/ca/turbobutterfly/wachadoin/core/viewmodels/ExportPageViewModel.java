package ca.turbobutterfly.wachadoin.core.viewmodels;

import android.provider.CalendarContract;

import java.util.Date;
import java.util.TimeZone;

import ca.turbobutterfly.core.mvvm.Command;
import ca.turbobutterfly.core.mvvm.CommandListener;
import ca.turbobutterfly.core.mvvm.ICommand;
import ca.turbobutterfly.core.utils.DateUtils;
import ca.turbobutterfly.core.utils.TextUtils;
import ca.turbobutterfly.core.viewmodels.ViewModel;

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
    private boolean _groupByDate;
    private String _logOrder;
    private int _snapTime;
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
    private final long _msPerHour = 1000 * 60 * 60;
    private final long _msPerDay = _msPerHour * 24;

    //  Constructors -------------------------------------------------------------------------------

    public ExportPageViewModel(
            IDataProvider dataProvider,
            IMainOptions mainOptions,
            ILogFileDeliveryFactory logFileDeliveryFactory)
    {
        _dataProvider = dataProvider;
        _mainOptions = mainOptions;
        _logFileDeliveryFactory = logFileDeliveryFactory;

        _groupByDate = _mainOptions.Export().group_by_date().Value();
        _logOrder = _mainOptions.Export().order().Value();
        _snapTime = _mainOptions.Export().snap().Value();
        _fileFormat = _mainOptions.Export().format().Value();
        _fileDelivery = _mainOptions.Export().delivery().Value();
        _emailAddress = _mainOptions.Export().email().Value();

        SetInitialDataRange();
    }

    private void SetInitialDataRange()
    {
        boolean use_reporting_period = _mainOptions.Display().use_reporting_period().Value();
        int reporting_period = _mainOptions.Display().reporting_period().Value();
        Date reporting_period_start = _mainOptions.Display().reporting_period_start().Value();
        int days_per_page = _mainOptions.Display().days_per_page().Value();

        int offset_ms = TimeZone.getDefault().getOffset(new Date().getTime());

        if (use_reporting_period)
        {
            long now_ms = new Date().getTime() + offset_ms;
            long rangeStart_ms = ((reporting_period_start.getTime() + offset_ms) / _msPerDay) * _msPerDay - offset_ms;
            while (rangeStart_ms < now_ms)
            {
                rangeStart_ms += reporting_period * _msPerDay;
            }
            while (rangeStart_ms > now_ms)
            {
                rangeStart_ms -= reporting_period * _msPerDay;
            }
            rangeStart_ms -= reporting_period * _msPerDay;
            long rangeEnd_ms = rangeStart_ms + (reporting_period - 1) * _msPerDay;
            _startTime = new Date(rangeStart_ms);
            _endTime = new Date(rangeEnd_ms);
        }
        else
        {
            long rangeEnd_ms = ((new Date().getTime() + offset_ms) / _msPerDay + 1) * _msPerDay - offset_ms;
            long rangeStart_ms = rangeEnd_ms - (days_per_page - 1) * _msPerDay;
            _endTime = new Date(rangeEnd_ms);
            _startTime = new Date(rangeStart_ms);
        }

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
        startTime = new Date((startTime.getTime() / _msPerHour) * _msPerHour);

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
        endTime = new Date((endTime.getTime() / _msPerHour) * _msPerHour);

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

    public Integer SnapTime()
    {
        return _snapTime;
    }

    public void SnapTime(Integer snapTime)
    {
        if (snapTime == _snapTime)
        {
            return;
        }
        _snapTime = snapTime;
        NotifyPropertyChanged("SnapTime");
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
        Date endTime = new Date(_endTime.getTime() + _msPerDay);

        return builder.BuildLogFile(_startTime, endTime, _groupByDate, _logOrder, _snapTime);
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
}
