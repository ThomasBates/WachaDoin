package ca.turbobutterfly.wachadoin.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import ca.turbobutterfly.events.EventHandler;
import ca.turbobutterfly.events.IEventArgs;
import ca.turbobutterfly.grid.Grid;
import ca.turbobutterfly.grid.GridColumn;
import ca.turbobutterfly.mvvm.IPropertyChangedEventArgs;
import ca.turbobutterfly.views.FragmentView;

import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.viewmodels.MainPageViewModel;
import ca.turbobutterfly.wachadoin.viewmodels.ViewPageViewModel;

public class ViewPage extends FragmentView
{
    private ViewPageViewModel _dataContext;
    private Grid _grid;

    private EventHandler _dataContextPropertyChangedEventHandler = new EventHandler()
    {
        @Override
        public void HandleEvent(Object sender, IEventArgs eventArgs)
        {
            String propertyName = ((IPropertyChangedEventArgs) eventArgs).PropertyName();

            switch (propertyName)
            {
                case "LogEntries":
                    _grid.SetDataSource(_dataContext.LogEntries());
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
        if (_grid == null)
        {
            return;
        }

        _grid.SetDataSource(_dataContext.LogEntries());
        _grid.Refresh();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.wachadoin_view_page, container, false);

        _grid = view.findViewById(R.id.grid);

        _grid.SetNoDataText("No data available");
        _grid.AddGridColumn(new GridColumn("Date", "Date", 220));
        _grid.AddGridColumn(new GridColumn("Start", "StartTime", 120));
        _grid.AddGridColumn(new GridColumn("End", "EndTime", 120));
        _grid.AddGridColumn(new GridColumn("Time", "TotalTime", 120));
        _grid.AddGridColumn(new GridColumn("Activity", "LogText", 2000));

        InitializeBindings();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        _grid.SetDataSource(_dataContext.LogEntries());
    }
}
