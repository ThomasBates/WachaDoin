package ca.turbobutterfly.mvvm;

import ca.turbobutterfly.events.Event;
import ca.turbobutterfly.events.IEvent;

public class Command implements ICommand
{
    private IEvent canExecuteChanged = new Event();
    private ICommandListener commandListener;

    @Override
    public IEvent CanExecuteChanged()
    {
        return canExecuteChanged;
    }

    @Override
    public boolean CanExecute(Object parameters)
    {
        return commandListener.CanExecute(parameters);
    }

    @Override
    public void Execute(Object parameters)
    {
        if (!CanExecute(parameters))
        {
            return;
        }

        commandListener.Execute(parameters);
    }

    public Command(ICommandListener commandListener)
    {
        this.commandListener = commandListener;
    }
}
