package ca.turbobutterfly.wachadoin.core.viewmodels;

import java.util.Date;
import java.util.TimeZone;

import ca.turbobutterfly.core.data.IDataColumn;
import ca.turbobutterfly.core.data.IDataRow;
import ca.turbobutterfly.core.data.IDataTable;
import ca.turbobutterfly.core.grid.IGridDataColumn;
import ca.turbobutterfly.core.grid.IGridDataRow;
import ca.turbobutterfly.core.grid.IGridDataRowEventArgs;
import ca.turbobutterfly.core.mvvm.Command;
import ca.turbobutterfly.core.mvvm.CommandListener;
import ca.turbobutterfly.core.mvvm.ICommand;
import ca.turbobutterfly.core.mvvm.PageViewModel;
import ca.turbobutterfly.core.utils.DateUtils;
import ca.turbobutterfly.core.utils.TextUtils;
import ca.turbobutterfly.wachadoin.core.data.IDataProvider;
import ca.turbobutterfly.wachadoin.core.options.IMainOptions;

public class EditPageViewModel extends PageViewModel
{
    //  Variables ----------------------------------------------------------------------------------

    //  Injected dependencies
    private IDataProvider _dataProvider;
    private IMainOptions _mainOptions;

    //  Property backers
    private String _pageName;
    private String _dateText;

    private ICommand _prevCommand = new Command(new CommandListener()
    {
        @Override
        public boolean CanExecute(Object parameter)
        {
            return CanDoPrev();
        }

        @Override
        public void Execute(Object parameter)
        {
            DoPrev();
        }
    });

    private ICommand _nextCommand = new Command(new CommandListener()
    {
        @Override
        public boolean CanExecute(Object parameter)
        {
            return CanDoNext();
        }

        @Override
        public void Execute(Object parameter)
        {
            DoNext();
        }
    });

    private ICommand _editRowCommand = new Command(new CommandListener()
    {
        @Override
        public void Execute(Object parameter)
        {
            DoEditRow(parameter);
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

    private String _logOrder;
    private Integer _roundTime;

    private IDataRow _dataRow;
    private IDataColumn _dataColumn;

    //  Constants
    private final long _msPerDay = 1000 * 60 * 60 * 24;

    //  Constructors -------------------------------------------------------------------------------

    public EditPageViewModel(
            IDataProvider dataProvider,
            IMainOptions mainOptions)
    {
        _dataProvider = dataProvider;
        _mainOptions = mainOptions;

        _logOrder = _mainOptions.Display().order().Value();
        _roundTime = 1; // _mainOptions.Display().round().Value();

        SetInitialDataRange();
    }

    private void SetInitialDataRange()
    {
        int offset_ms = TimeZone.getDefault().getOffset(new Date().getTime());

        long rangeStart_ms = (new Date().getTime() + offset_ms) / _msPerDay * _msPerDay - offset_ms;
        long rangeEnd_ms = rangeStart_ms + _msPerDay;
        _rangeEnd = new Date(rangeEnd_ms);
        _rangeStart = new Date(rangeStart_ms);

        _minRangeStart = new Date(0);
        _maxRangeEnd = _rangeEnd;

        DateText(DateUtils.LongDate(_rangeStart));
    }

    //  Properties ---------------------------------------------------------------------------------

    public String DateText()
    {
        return _dateText;
    }

    public void DateText(String dateText)
    {
        if (TextUtils.equals(_dateText, dateText))
        {
            return;
        }
        _dateText = dateText;
        NotifyPropertyChanged("DateText");
    }

    public IDataTable LogEntries()
    {
        IDataTable logEntries = _dataProvider.GetLogEntries(_rangeStart, _rangeEnd, false, _roundTime);

        if (logEntries.RowCount() == 0)
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

        return logEntries;
    }

    public String[] IndexFields()
    {
        if ((_logOrder != null) && (_logOrder.equals("Descending")))
        {
            return new String[]{"EndTime DESC", "StartTime DESC"};
        }

        return new String[]{"EndTime", "StartTime"};
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
        if ((_dataRow == null) || (_dataColumn == null))
        {
            return;
        }

        Object value = EditValue();

        if ((value == null) ? (editValue == null) : value.equals(editValue))
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

        if (!updated)
        {
            return;
        }

        NotifyPropertyChanged("LogEntries");
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

    public ICommand EditRowCommand()
    {
        return _editRowCommand;
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
        long rangeEnd_ms = _rangeEnd.getTime();
        long rangeStart_ms = rangeEnd_ms - _msPerDay;
        _rangeStart = new Date(rangeStart_ms);

        DateText(DateUtils.LongDate(_rangeStart));
    }

    private boolean CanDoNext()
    {
        return _rangeEnd.before(_maxRangeEnd);
    }

    private void DoNext()
    {
        _rangeStart = _rangeEnd;
        long rangeStart_ms = _rangeStart.getTime();
        long rangeEnd_ms = rangeStart_ms + _msPerDay;
        _rangeEnd = new Date(rangeEnd_ms);

        DateText(DateUtils.LongDate(_rangeStart));
    }

    private void DoEditRow(Object parameter)
    {
    }

    private void DoEditCell(Object parameter)
    {
        IGridDataRowEventArgs eventArgs = (IGridDataRowEventArgs)parameter;

        IGridDataRow gridDataRow = eventArgs.Row();
        _dataRow = gridDataRow.DataRow();

        IGridDataColumn gridDataColumn = eventArgs.Column();
        String columnName = gridDataColumn.Name();

        switch (columnName)
        {
            case "DisplayStartTime":
                _dataColumn = _dataRow.DataTable().Column("StartTime");
                NotifyPropertyChanged("EditStartTime");
                break;
            case "DisplayEndTime":
                _dataColumn = _dataRow.DataTable().Column("EndTime");
                NotifyPropertyChanged("EditEndTime");
                break;
            case "LogText":
                _dataColumn = _dataRow.DataTable().Column("LogText");
                NotifyPropertyChanged("EditLogText");
                break;
        }
    }
}
