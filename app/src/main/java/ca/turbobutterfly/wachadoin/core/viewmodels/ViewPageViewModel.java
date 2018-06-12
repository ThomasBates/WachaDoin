package ca.turbobutterfly.wachadoin.core.viewmodels;

import java.util.Date;
import java.util.TimeZone;

import ca.turbobutterfly.core.data.IDataTable;
import ca.turbobutterfly.core.mvvm.Command;
import ca.turbobutterfly.core.mvvm.CommandListener;
import ca.turbobutterfly.core.mvvm.ICommand;
import ca.turbobutterfly.core.utils.DateUtils;
import ca.turbobutterfly.core.utils.TextUtils;
import ca.turbobutterfly.core.viewmodels.ViewModel;

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
    private String _order;
    private Integer _snap;

    //  Constants
    private final long _msPerDay = 1000 * 60 * 60 * 24;

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
        _order = _mainOptions.Display().order().Value();
        _snap = _mainOptions.Display().snap().Value();

        SetInitialDataRange();
    }

    private void SetInitialDataRange()
    {
        int offset_ms = TimeZone.getDefault().getOffset(new Date().getTime());

        if (_use_reporting_period)
        {
            //long startDay_ms = (startTime.getTime() + offset_ms) / _msPerDay;
            long now_ms = new Date().getTime() + offset_ms;
            long rangeStart_ms = ((_reporting_period_start.getTime() + offset_ms) / _msPerDay) * _msPerDay - offset_ms;
            while (rangeStart_ms < now_ms)
            {
                rangeStart_ms += _reporting_period * _msPerDay;
            }
            while (rangeStart_ms > now_ms)
            {
                rangeStart_ms -= _reporting_period * _msPerDay;
            }
            long rangeEnd_ms = rangeStart_ms + _reporting_period * _msPerDay;
            _rangeStart = new Date(rangeStart_ms);
            _rangeEnd = new Date(rangeEnd_ms);
        }
        else
        {
            long rangeEnd_ms = ((new Date().getTime() + offset_ms) / _msPerDay + 1) * _msPerDay - offset_ms;
            long rangeStart_ms = rangeEnd_ms - _days_per_page * _msPerDay;
            _rangeEnd = new Date(rangeEnd_ms);
            _rangeStart = new Date(rangeStart_ms);
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
        IDataTable logEntries = _dataProvider.GetLogEntries(_rangeStart, _rangeEnd);

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
        if ((_order != null) && (_order.equals("Descending")))
        {
            return new String[]{"EndTime DESC", "StartTime DESC"};
        }

        return new String[]{"EndTime", "StartTime"};
    }

    public ICommand PrevCommand()
    {
        return _prevCommand;
    }

    public ICommand NextCommand()
    {
        return _nextCommand;
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
        int daysPerPage = _days_per_page;

        if (_use_reporting_period)
        {
            daysPerPage = _reporting_period;
        }

        _rangeEnd = _rangeStart;
        long rangeEnd_ms = _rangeEnd.getTime();
        long rangeStart_ms = rangeEnd_ms - daysPerPage * _msPerDay;
        _rangeStart = new Date(rangeStart_ms);

        DateRangeText(DateUtils.LongDate(_rangeStart));
    }

    private boolean CanDoNext()
    {
        return _rangeEnd.before(_maxRangeEnd);
    }

    private void DoNext()
    {
        int daysPerPage = _days_per_page;

        if (_use_reporting_period)
        {
            daysPerPage = _reporting_period;
        }

        _rangeStart = _rangeEnd;
        long rangeStart_ms = _rangeStart.getTime();
        long rangeEnd_ms = rangeStart_ms + daysPerPage * _msPerDay;
        _rangeEnd = new Date(rangeEnd_ms);

        DateRangeText(DateUtils.LongDate(_rangeStart));
    }
}
