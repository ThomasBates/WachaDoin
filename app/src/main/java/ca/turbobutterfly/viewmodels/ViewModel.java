package ca.turbobutterfly.viewmodels;

import ca.turbobutterfly.events.Event;
import ca.turbobutterfly.events.IEvent;
import ca.turbobutterfly.mvvm.INotifyPropertyChanged;
import ca.turbobutterfly.mvvm.PropertyChangedEventArgs;

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
