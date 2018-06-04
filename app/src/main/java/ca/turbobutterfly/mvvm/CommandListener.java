package ca.turbobutterfly.mvvm;

public abstract class CommandListener implements ICommandListener
{
    @Override
    public boolean CanExecute(Object parameters)
    {
        return true;
    }
}
