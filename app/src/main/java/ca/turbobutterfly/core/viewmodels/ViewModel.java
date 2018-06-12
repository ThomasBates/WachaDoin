package ca.turbobutterfly.core.viewmodels;

import ca.turbobutterfly.core.events.Event;
import ca.turbobutterfly.core.events.IEvent;
import ca.turbobutterfly.core.mvvm.INotifyPropertyChanged;
import ca.turbobutterfly.core.mvvm.PropertyChangedEventArgs;

public class ViewModel implements INotifyPropertyChanged
{
    private IEvent _propertyChanged = new Event();

    @Override
    public IEvent PropertyChanged()
    {
        return _propertyChanged;
    }

    protected void NotifyPropertyChanged(String propertyName)
    {
        _propertyChanged.Publish(this, new PropertyChangedEventArgs(propertyName));
    }
}
