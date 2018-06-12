package ca.turbobutterfly.wachadoin.android.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.turbobutterfly.core.events.EventHandler;
import ca.turbobutterfly.core.events.IEventArgs;
import ca.turbobutterfly.core.mvvm.IPropertyChangedEventArgs;
import ca.turbobutterfly.android.views.FragmentView;
import ca.turbobutterfly.wachadoin.R;
import ca.turbobutterfly.wachadoin.core.viewmodels.ExportPageViewModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class ExportPage extends FragmentView
{
    private ExportPageViewModel _dataContext;

    private EventHandler _dataContextPropertyChangedEventHandler = new EventHandler()
    {
        @Override
        public void HandleEvent(Object sender, IEventArgs eventArgs)
        {
            String propertyName = ((IPropertyChangedEventArgs) eventArgs).PropertyName();

            switch (propertyName)
            {
                case "LogEntries":
                    break;
            }
        }
    };

    @Override
    public String toString()
    {
        return "Export Log";
    }

    @Override
    public void DataContext(Object dataContext)
    {
        if (_dataContext != null)
        {
            _dataContext.PropertyChanged().Unsubscribe(_dataContextPropertyChangedEventHandler);
        }

        _dataContext = (ExportPageViewModel) dataContext;

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
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.export_page, container, false);
    }
}
