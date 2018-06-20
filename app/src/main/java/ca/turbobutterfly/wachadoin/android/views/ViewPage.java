package ca.turbobutterfly.wachadoin.android.views;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import ca.turbobutterfly.android.utils.ReturnValueDelegate;
import ca.turbobutterfly.android.utils.ViewUtils;
import ca.turbobutterfly.core.events.EventHandler;
import ca.turbobutterfly.core.events.IEventArgs;
import ca.turbobutterfly.android.grid.Grid;
import ca.turbobutterfly.core.events.IEventHandler;
import ca.turbobutterfly.core.grid.GridDataSource;
import ca.turbobutterfly.core.mvvm.IPropertyChangedEventArgs;
import ca.turbobutterfly.android.views.FragmentView;

import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.core.viewmodels.ViewPageViewModel;

public class ViewPage extends FragmentView
{
    private ViewPageViewModel _dataContext;

    private Button _prevButton;
    private Button _nextButton;
    private TextView _rangeTextView;
    private Grid _grid;
    private int _topPosition = -1;

    private EventHandler _dataContextPropertyChangedEventHandler = new EventHandler()
    {
        @Override
        public void HandleEvent(Object sender, IEventArgs eventArgs)
        {
            String propertyName = ((IPropertyChangedEventArgs) eventArgs).PropertyName();

            switch (propertyName)
            {
                case "DateRangeText":
                    _topPosition = -1;
                    _rangeTextView.setText(_dataContext.DateRangeText());
                    RequeryLogEntries();
                    break;

                case "LogEntries":
                    RequeryLogEntries();
                    break;

                case "EditStartTime":
                    _topPosition = _grid.GetTopPosition();
                    OpenTimePicker(R.string.view_page_edit_start_time);
                    break;

                case "EditEndTime":
                    _topPosition = _grid.GetTopPosition();
                    OpenTimePicker(R.string.view_page_edit_end_time);
                    break;

                case "EditLogText":
                    _topPosition = _grid.GetTopPosition();
                    OpenInputDialog(R.string.view_page_edit_log_text);
                    break;
            }
        }
    };

    @Override
    public String toString()
    {
        return "View Log";
    }

    @Override
    public void DataContext(Object dataContext)
    {
        if (_dataContext != null)
        {
            _dataContext.OnPropertyChanged().Unsubscribe(_dataContextPropertyChangedEventHandler);
        }

        _dataContext = (ViewPageViewModel) dataContext;

        if (_dataContext != null)
        {
            _dataContext.OnPropertyChanged().Subscribe(_dataContextPropertyChangedEventHandler);
        }

        InitializeBindings();
    }

    private void InitializeBindings()
    {
        if (_dataContext == null)
        {
            return;
        }
        if (_rangeTextView == null)
        {
            return;
        }

        _rangeTextView.setText(_dataContext.DateRangeText());

        RequeryLogEntries();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.view_page, container, false);

        _rangeTextView = view.findViewById(R.id.rangeTextView);

        _prevButton = view.findViewById(R.id.prevButton);
        _prevButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (_dataContext != null)
                {
                    _dataContext.PrevCommand().Execute(null);
                }
            }
        });

        _nextButton = view.findViewById(R.id.nextButton);
        _nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (_dataContext != null)
                {
                    _dataContext.NextCommand().Execute(null);
                }
            }
        });

        Paint paint = _rangeTextView.getPaint();
        int dateWidth = 20 + (int) paint.measureText("0000-00-00");
        int timeWidth = 20 + (int) paint.measureText("00:00");
        int textWidth = 2000;

        _grid = view.findViewById(R.id.grid);

        _grid.AddGridColumn("Date", "DisplayDate", dateWidth);
        _grid.AddGridColumn("Start", "DisplayStartTime", timeWidth);
        _grid.AddGridColumn("End", "DisplayEndTime", timeWidth);
        _grid.AddGridColumn("Total", "DisplayTotalTime", timeWidth);
        _grid.AddGridColumn("Activity", "LogText", textWidth);
        _grid.OnCellLongClick().Subscribe(new IEventHandler()
        {
            @Override
            public void HandleEvent(Object sender, IEventArgs eventArgs)
            {
                _dataContext.EditCellCommand().Execute(eventArgs);
            }
        });

        InitializeBindings();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        RequeryLogEntries();
    }

    private void RequeryLogEntries()
    {
        GridDataSource dataSource = new GridDataSource(_dataContext.LogEntries());
        dataSource.GroupFields(_dataContext.GroupFields());
        dataSource.IndexFields(_dataContext.IndexFields());

        _grid.SetDataSource(dataSource);

        UpdateCommands();

        if (_topPosition < 0)
        {
            _topPosition = _dataContext.DefaultTopPosition();
        }
        _grid.SetTopPosition(_topPosition);
    }

    private void UpdateCommands()
    {
        if (_dataContext == null)
        {
            return;
        }

        _prevButton.setEnabled(_dataContext.PrevCommand().CanExecute(null));
        _nextButton.setEnabled(_dataContext.NextCommand().CanExecute(null));
    }

    private void OpenTimePicker(int titleID)
    {
        Date value = (Date)_dataContext.EditValue();

        ViewUtils.OpenTimePicker(this,
                titleID,
                value,
                new ReturnValueDelegate()
                {
                    @Override
                    public void HandleReturnValue(Object value)
                    {
                        _dataContext.EditValue(value);
                    }
                });
    }

    private void OpenInputDialog(int titleID)
    {
        String value = (String)_dataContext.EditValue();

        ViewUtils.OpenInputDialog(this,
                titleID,
                value,
                new ReturnValueDelegate()
                {
                    @Override
                    public void HandleReturnValue(Object value)
                    {
                        _dataContext.EditValue(value);
                    }
                });
    }
}
