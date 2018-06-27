package ca.turbobutterfly.core.mvvm;

import ca.turbobutterfly.core.events.IEvent;

public interface INotifyPropertyChanged
{
    IEvent OnPropertyChanged();
}
