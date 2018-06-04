package ca.turbobutterfly.mvvm;

import ca.turbobutterfly.events.IEvent;

public interface INotifyPropertyChanged
{
    public IEvent PropertyChanged();
}
