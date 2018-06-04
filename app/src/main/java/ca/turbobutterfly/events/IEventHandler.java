package ca.turbobutterfly.events;

public interface IEventHandler
{
    public void HandleEvent(Object sender, IEventArgs eventArgs);
}
