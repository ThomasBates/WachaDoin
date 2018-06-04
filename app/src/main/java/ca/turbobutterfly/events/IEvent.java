package ca.turbobutterfly.events;

public interface IEvent
{
    public int EventHandlerCount();

    public void Subscribe(IEventHandler eventHandler);

    public void Unsubscribe(IEventHandler eventHandler);

    public void Publish(Object sender, IEventArgs eventArgs);
}
