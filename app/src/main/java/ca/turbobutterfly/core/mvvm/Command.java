package ca.turbobutterfly.core.mvvm;

import ca.turbobutterfly.core.events.Event;
import ca.turbobutterfly.core.events.IEvent;

public class Command implements ICommand
{
    private IEvent _canExecuteChanged = new Event();
    private ICommandListener _commandListener;

    public Command(ICommandListener commandListener)
    {
        _commandListener = commandListener;
    }

    @Override
    public IEvent CanExecuteChanged()
    {
        return _canExecuteChanged;
    }

    @Override
    public boolean CanExecute(Object parameter)
    {
        return _commandListener.CanExecute(parameter);
    }

    @Override
    public void Execute(Object parameter)
    {
        if (!CanExecute(parameter))
        {
            return;
        }

        _commandListener.Execute(parameter);
    }
}
