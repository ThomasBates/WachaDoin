package ca.turbobutterfly.core.mvvm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.turbobutterfly.core.events.Event;
import ca.turbobutterfly.core.events.IEvent;
import ca.turbobutterfly.core.utils.TextUtils;

public class ViewModel implements INotifyPropertyChanged
{
    private IEvent _onPropertyChanged = new Event();

    @Override
    public IEvent OnPropertyChanged()
    {
        return _onPropertyChanged;
    }

    protected void NotifyPropertyChanged(String propertyName)
    {
        _onPropertyChanged.Publish(this, new PropertyChangedEventArgs(propertyName));
    }
}
