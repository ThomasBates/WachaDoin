package ca.turbobutterfly.core.mvvm;

import ca.turbobutterfly.core.events.IEvent;

public interface ICommand
{
    public IEvent CanExecuteChanged();

    public boolean CanExecute(Object parameters);

    public void Execute(Object parameters);
}
