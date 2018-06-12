package ca.turbobutterfly.core.mvvm;

import ca.turbobutterfly.core.events.IEventArgs;

public interface IPropertyChangedEventArgs extends IEventArgs
{
    public String PropertyName();
}
