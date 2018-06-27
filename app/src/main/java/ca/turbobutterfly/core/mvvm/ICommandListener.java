package ca.turbobutterfly.core.mvvm;

public interface ICommandListener
{
    boolean CanExecute(Object parameters);
    void Execute(Object parameters);
}
