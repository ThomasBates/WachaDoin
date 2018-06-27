package ca.turbobutterfly.core.mvvm;

import ca.turbobutterfly.core.events.IEvent;

public interface ICommand
{
    IEvent CanExecuteChanged();
    boolean CanExecute(Object parameters);
    void Execute(Object parameters);
}
