package ca.turbobutterfly.wachadoin.core.viewmodels;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import ca.turbobutterfly.core.data.IDataColumn;
import ca.turbobutterfly.core.data.IDataRow;
import ca.turbobutterfly.core.data.IDataTable;
import ca.turbobutterfly.core.grid.IGridDataColumn;
import ca.turbobutterfly.core.grid.IGridDataRowEventArgs;
import ca.turbobutterfly.core.mvvm.Command;
import ca.turbobutterfly.core.mvvm.CommandListener;
import ca.turbobutterfly.core.mvvm.ICommand;
import ca.turbobutterfly.core.utils.DateUtils;
import ca.turbobutterfly.core.utils.TextUtils;
import ca.turbobutterfly.core.mvvm.ViewModel;

import ca.turbobutterfly.wachadoin.core.data.IDataProvider;
import ca.turbobutterfly.wachadoin.core.options.IMainOptions;

public class ViewPageViewModel extends ViewModel
{
    //  Variables ----------------------------------------------------------------------------------

    //  Injected dependencies
    private IDataProvider _dataProvider;
    private IMainOptions _mainOptions;

    //  Property backers
    private String _dateRangeText;
    private IDataTable _logEntries;

    private ICommand _prevCommand = new Command(new CommandListener()
    {
        @Override
        public boolean CanExecute(Object parameters)
        {
            return CanDoPrev();
        }

        @Override
        public void Execute(Object parameters)
        {
            DoPrev();
        }
    });

    private ICommand _nextCommand = new Command(new CommandListener()
    {
        @Override
        public boolean CanExecute(Object parameters)
        {
            return CanDoNext();
        }

        @Override
        public void Execute(Object parameters)
        {
            DoNext();
        }
    });

    private ICommand _editCellCommand = new Command(new CommandListener()
    {
        @Override
        public void Execute(Object parameter)
        {
            DoEditCell(parameter);
        }
    });

    //  Internal
    private Date _rangeStart;
    private Date _rangeEnd;
    private Date _minRangeStart;
    private Date _maxRangeEnd;

    private Boolean _use_reporting_period;
    private Integer _reporting_period;
    private Date _reporting_period_start;
    private Integer _days_per_page;
    private Boolean _group_by_date;
    private String _logOrder;
    private Integer _roundTime;

    private IDataRow _dataRow;
    private IDataColumn _dataColumn;

    private boolean _insertingRow;
    private Date _insertStartTime;
    private Date _insertEndTime;
    private String _insertLogText;

    //  Constants
//    private final long _msPerDay = 1000 * 60 * 60 * 24;

    //  Constructors -------------------------------------------------------------------------------

    public ViewPageViewModel(
            IDataProvider dataProvider,
            IMainOptions mainOptions)
    {
        _dataProvider = dataProvider;
        _mainOptions = mainOptions;

        _use_reporting_period = _mainOptions.Display().use_reporting_period().Value();
        _reporting_period = _mainOptions.Display().reporting_period().Value();
        _reporting_period_start = _mainOptions.Display().reporting_period_start().Value();
        _days_per_page = _mainOptions.Display().days_per_page().Value();
        _group_by_date = _mainOptions.Display().group_by_date().Value();
        _logOrder = _mainOptions.Display().order().Value();
        _roundTime = _mainOptions.Display().round().Value();

        SetInitialDataRange();
    }

    private void SetInitialDataRange()
    {
        if (_use_reporting_period)
        {
            Date now = new Date();
            _rangeStart = _reporting_period_start;
            while (_rangeStart.before(now))
            {
                _rangeStart = IncrementDate(_rangeStart);
            }
            while (_rangeStart.after(now))
            {
                _rangeStart = DecrementDate(_rangeStart);
            }
            _rangeEnd = IncrementDate(_rangeStart);

            //  Update reporting_period_start if we're in the next reporting period.
            //  It makes no difference to program operation, but it will show the new date on the Settings screen.
            if (!DateUtils.equals(_rangeStart, _reporting_period_start))
            {
                _reporting_period_start = _rangeStart;
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
            _rangeEnd = calendar.getTime();
            _rangeStart = DecrementDate(_rangeEnd);
        }

        _minRangeStart = new Date(0);
        _maxRangeEnd = _rangeEnd;

        DateRangeText(DateUtils.LongDate(_rangeStart));
    }

    //  Properties ---------------------------------------------------------------------------------

    public String DateRangeText()
    {
        return _dateRangeText;
    }

    public void DateRangeText(String dateRangeText)
    {
        if (TextUtils.equals(_dateRangeText, dateRangeText))
        {
            return;
        }
        _dateRangeText = dateRangeText;
        NotifyPropertyChanged("DateRangeText");
    }

    public IDataTable LogEntries()
    {
        _logEntries = _dataProvider.GetLogEntries(
                _rangeStart,
                _rangeEnd,
                _group_by_date,
                _roundTime);

        if (_logEntries.RowCount() == 0)
        {
            if (_rangeEnd.equals(_maxRangeEnd))
            {
                _minRangeStart = _rangeStart;
            }
            else
            {
                _minRangeStart = _rangeEnd;
            }
        }

        return _logEntries;
    }

    public String[] GroupFields()
    {
        if (_group_by_date)
        {
            return new String[]{"DisplayDate"};
        }

        return new String[0];
    }

    public String[] IndexFields()
    {
        if (TextUtils.equals(_logOrder,"Descending"))
        {
            return new String[]{"EndTime DESC", "StartTime DESC"};
        }

        return new String[]{"EndTime", "StartTime"};
    }

    public int DefaultTopPosition()
    {
        if (TextUtils.equals(_logOrder,"Descending"))
        {
            return 0;
        }

        return _logEntries.RowCount();
    }

    public Object EditValue()
    {
        if ((_dataRow == null) || (_dataColumn == null))
        {
            return null;
        }

        return _dataRow.Value(_dataColumn);
    }

    public void EditValue(Object editValue)
    {
        if (_insertingRow)
        {
            InsertValue(editValue);
            return;
        }

        if ((_dataRow == null) || (_dataColumn == null))
        {
            return;
        }

        Object oldValue = EditValue();

        if ((oldValue == null) ? (editValue == null) : oldValue.equals(editValue))
        {
            return;
        }

        Date startTime = (Date)_dataRow.Value("StartTime");
        Date endTime = (Date)_dataRow.Value("EndTime");
        String logText = (String)_dataRow.Value("LogText");

        switch (_dataColumn.Name())
        {
            case "StartTime":
                Date newStartTime = (Date)editValue;
                if (DateUtils.equals(startTime, newStartTime))
                {
                    return;
                }
                startTime = newStartTime;
                break;

            case "EndTime":
                Date newEndTime = (Date)editValue;

                //  If the user selected 00:00, then set it to the
                //  end of the day, instead of the start of the day.
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(newEndTime);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                if ((hour == 0) && (minute == 0))
                {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    newEndTime = calendar.getTime();
                }

                if (DateUtils.equals(endTime, newEndTime))
                {
                    return;
                }
                endTime = newEndTime;
                break;

            case "LogText":
                String newLogText = (String)editValue;
                if (TextUtils.equals(logText, newLogText))
                {
                    return;
                }
                logText = newLogText;
                break;
        }

        if (!startTime.before(endTime))
        {
            return;
        }

        boolean updated = _dataProvider.UpdateLogEntry(startTime, endTime, logText, _dataColumn.Name());

        _dataRow = null;
        _dataColumn = null;

        if (updated)
        {
            NotifyPropertyChanged("LogEntries");
        }
    }

    private void InsertValue(Object editValue)
    {
        if ((_dataRow == null) || (_dataColumn == null))
        {
            return;
        }

        switch (_dataColumn.Name())
        {
            case "StartTime":
                _insertStartTime = (Date)editValue;

                _dataColumn = _dataRow.DataTable().Column("EndTime");
                NotifyPropertyChanged("EditEndTime");
                return;

            case "EndTime":
                _insertEndTime = (Date)editValue;

                //  If the user selected 00:00, then set it to the
                //  end of the day, instead of the start of the day.
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(_insertEndTime);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                if ((hour == 0) && (minute == 0))
                {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    _insertEndTime = calendar.getTime();
                }

                if (!_insertStartTime.before(_insertEndTime))
                {
                    return;
                }

                _dataColumn = _dataRow.DataTable().Column("LogText");
                NotifyPropertyChanged("EditLogText");
                return;

            case "LogText":
                _insertLogText = (String)editValue;

                boolean updated = _dataProvider.SaveLogEntry(
                        _insertStartTime,
                        _insertEndTime,
                        _insertLogText);

                _dataRow = null;
                _dataColumn = null;

                if (updated)
                {
                    NotifyPropertyChanged("LogEntries");
                }
                break;
        }
    }

    public ICommand PrevCommand()
    {
        return _prevCommand;
    }

    public ICommand NextCommand()
    {
        return _nextCommand;
    }

    public ICommand EditCellCommand()
    {
        return _editCellCommand;
    }

    //  Methods ------------------------------------------------------------------------------------

    private boolean CanDoPrev()
    {
        if (_rangeStart.before(_minRangeStart))
        {
            DoNext();
            return false;
        }

        return _rangeStart.after(_minRangeStart);
    }

    private void DoPrev()
    {
        _rangeEnd = _rangeStart;
        _rangeStart = DecrementDate(_rangeEnd);

        DateRangeText(DateUtils.LongDate(_rangeStart));
    }

    private boolean CanDoNext()
    {
        return _rangeEnd.before(_maxRangeEnd);
    }

    private void DoNext()
    {
        _rangeStart = _rangeEnd;
        _rangeEnd = IncrementDate(_rangeStart);

        DateRangeText(DateUtils.LongDate(_rangeStart));
    }

    private void DoEditCell(Object parameter)
    {
        IGridDataRowEventArgs eventArgs = (IGridDataRowEventArgs)parameter;

        _dataRow = eventArgs.Row().DataRow();
        IGridDataColumn column = eventArgs.Column();
        String columnName;
        if (column != null)
        {
            columnName = column.Name();
        }
        else
        {
            columnName = "DisplayDate";
        }

        _insertingRow = false;

        switch (columnName)
        {
            case "DisplayDate":
                _insertingRow = true;
                _dataColumn = _dataRow.DataTable().Column("StartTime");
                NotifyPropertyChanged("EditStartTime");
                break;

            case "DisplayStartTime":
                String displayTime = (String)_dataRow.Value(columnName);
                if (!TextUtils.equals(displayTime, "00:00"))
                {
                    _dataColumn = _dataRow.DataTable().Column("StartTime");
                    NotifyPropertyChanged("EditStartTime");
                }
                break;

            case "DisplayEndTime":
                displayTime = (String)_dataRow.Value(columnName);
                if (!TextUtils.equals(displayTime, "24:00"))
                {
                    _dataColumn = _dataRow.DataTable().Column("EndTime");
                    NotifyPropertyChanged("EditEndTime");
                }
                break;

            case "LogText":
                _dataColumn = _dataRow.DataTable().Column("LogText");
                NotifyPropertyChanged("EditLogText");
                break;
        }
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
}
