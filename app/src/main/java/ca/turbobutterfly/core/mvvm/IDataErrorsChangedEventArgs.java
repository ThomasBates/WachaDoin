package ca.turbobutterfly.core.mvvm;

import ca.turbobutterfly.core.events.IEventArgs;

public interface IDataErrorsChangedEventArgs extends IEventArgs
{
    String PropertyName();
}
