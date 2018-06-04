package ca.turbobutterfly.mvvm;

import ca.turbobutterfly.events.IEvent;

public interface ICommand
{
    public IEvent CanExecuteChanged();

    public boolean CanExecute(Object parameters);

    public void Execute(Object parameters);
}
