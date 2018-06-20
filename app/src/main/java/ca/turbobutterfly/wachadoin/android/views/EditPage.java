package ca.turbobutterfly.wachadoin.android.views;

import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import ca.turbobutterfly.android.grid.Grid;
import ca.turbobutterfly.android.utils.ReturnValueDelegate;
import ca.turbobutterfly.android.utils.ViewUtils;
import ca.turbobutterfly.android.views.FragmentView;

import ca.turbobutterfly.core.events.EventHandler;
import ca.turbobutterfly.core.events.IEventArgs;
import ca.turbobutterfly.core.events.IEventHandler;
import ca.turbobutterfly.core.grid.GridDataSource;
import ca.turbobutterfly.core.mvvm.IPropertyChangedEventArgs;
import ca.turbobutterfly.core.utils.DateUtils;

import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.core.viewmodels.EditPageViewModel;

public class EditPage extends FragmentView
{
    private EditPageViewModel _dataContext;

    private Button _prevButton;
    private Button _nextButton;
    private TextView _dateTextView;
    private Grid _grid;

    private EventHandler _dataContextPropertyChangedEventHandler = new EventHandler()
    {
        @Override
        public void HandleEvent(Object sender, IEventArgs eventArgs)
        {
            String propertyName = ((IPropertyChangedEventArgs) eventArgs).PropertyName();

            switch (propertyName)
            {
                case "DateText":
                    _dateTextView.setText(_dataContext.DateText());
                    RequeryLogEntries();
                    break;

                case "LogEntries":
                    RequeryLogEntries();
                    break;

                case "EditStartTime":
                    OpenTimePicker(R.string.edit_page_start_time);
                    break;

                case "EditEndTime":
                    OpenTimePicker(R.string.edit_page_end_time);
                    break;

                case "EditLogText":
                    OpenInputDialog(R.string.edit_page_log_text);
                    break;
            }
        }
    };

    @Override
    public String toString()
    {
        return "Edit Log";
    }

    @Override
    public void DataContext(Object dataContext)
    {
        if (_dataContext != null)
        {
            _dataContext.OnPropertyChanged().Unsubscribe(_dataContextPropertyChangedEventHandler);
        }

        _dataContext = (EditPageViewModel) dataContext;

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
        if (_dateTextView == null)
        {
            return;
        }

        _dateTextView.setText(_dataContext.DateText());

        RequeryLogEntries();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.edit_page, container, false);

        _dateTextView = view.findViewById(R.id.dateTextView);

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

        Paint paint = _dateTextView.getPaint();
        int dateWidth = 20 + (int) paint.measureText("0000-00-00");
        int timeWidth = 20 + (int) paint.measureText("00:00");
        int textWidth = 2000;

        _grid = view.findViewById(R.id.grid);

        _grid.AddGridColumn("Date", "DisplayDate", dateWidth);
        _grid.AddGridColumn("Start", "DisplayStartTime", timeWidth);
        _grid.AddGridColumn("End", "DisplayEndTime", timeWidth);
        _grid.AddGridColumn("Total", "DisplayTotalTime", timeWidth);
        _grid.AddGridColumn("Activity", "LogText", textWidth);
        _grid.OnRowClick().Subscribe(new IEventHandler()
        {
            @Override
            public void HandleEvent(Object sender, IEventArgs eventArgs)
            {
                _dataContext.EditRowCommand().Execute(eventArgs);
            }
        });
        _grid.OnCellClick().Subscribe(new IEventHandler()
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

    @Override
    public void onResume()
    {
        super.onResume();

        RequeryLogEntries();
    }

    private void RequeryLogEntries()
    {
        GridDataSource dataSource = new GridDataSource(_dataContext.LogEntries());
        dataSource.IndexFields(_dataContext.IndexFields());

        _grid.SetDataSource(dataSource);

        UpdateCommands();
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

}
