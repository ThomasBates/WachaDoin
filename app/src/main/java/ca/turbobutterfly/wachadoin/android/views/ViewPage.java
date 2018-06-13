package ca.turbobutterfly.wachadoin.android.views;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ca.turbobutterfly.core.events.EventHandler;
import ca.turbobutterfly.core.events.IEventArgs;
import ca.turbobutterfly.android.grid.Grid;
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

    private EventHandler _dataContextPropertyChangedEventHandler = new EventHandler()
    {
        @Override
        public void HandleEvent(Object sender, IEventArgs eventArgs)
        {
            String propertyName = ((IPropertyChangedEventArgs) eventArgs).PropertyName();

            switch (propertyName)
            {
                case "DateRangeText":
                    _rangeTextView.setText(_dataContext.DateRangeText());
                    RequeryLogEntries();
                    break;

                case "LogEntries":
                    RequeryLogEntries();
                    break;
            }
        }
    };

    @Override
    public String toString()
    {
        return "DataView Log";
    }

    @Override
    public void DataContext(Object dataContext)
    {
        if (_dataContext != null)
        {
            _dataContext.PropertyChanged().Unsubscribe(_dataContextPropertyChangedEventHandler);
        }

        _dataContext = (ViewPageViewModel) dataContext;

        if (_dataContext != null)
        {
            _dataContext.PropertyChanged().Subscribe(_dataContextPropertyChangedEventHandler);
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

        _grid.Refresh();
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
        _grid.SetNoDataText("No data available");

        _grid.AddGridColumn("Date", "DisplayDate", dateWidth);
        _grid.AddGridColumn("Start", "DisplayStartTime", timeWidth);
        _grid.AddGridColumn("End", "DisplayEndTime", timeWidth);
        _grid.AddGridColumn("Total", "DisplayTotalTime", timeWidth);
        _grid.AddGridColumn("Activity", "LogText", textWidth);

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
