package ca.turbobutterfly.mvvm;

import ca.turbobutterfly.events.IEventArgs;

public interface IPropertyChangedEventArgs extends IEventArgs
{
    public String PropertyName();
}
