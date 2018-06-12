package ca.turbobutterfly.core.events;

public interface IEventHandler
{
    public void HandleEvent(Object sender, IEventArgs eventArgs);
}
