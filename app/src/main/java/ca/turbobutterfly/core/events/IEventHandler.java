package ca.turbobutterfly.core.events;

public interface IEventHandler
{
    void HandleEvent(Object sender, IEventArgs eventArgs);
}
