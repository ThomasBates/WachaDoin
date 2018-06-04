package ca.turbobutterfly.mvvm;

public interface ICommandListener
{
    public boolean CanExecute(Object parameters);

    public void Execute(Object parameters);
}
