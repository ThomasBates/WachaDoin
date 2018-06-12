package ca.turbobutterfly.core.events;

import java.util.ArrayList;

public class Event implements IEvent
{
    private ArrayList<IEventHandler> _eventHandlers = new ArrayList<IEventHandler>();

    @Override
    public int EventHandlerCount()
    {
        return _eventHandlers.size();
    }

    @Override
    public void Subscribe(IEventHandler eventHandler)
    {
        if (!_eventHandlers.contains(eventHandler))
        {
            _eventHandlers.add(eventHandler);
        }
    }

    @Override
    public void Unsubscribe(IEventHandler eventHandler)
    {
        if (_eventHandlers.contains(eventHandler))
        {
            _eventHandlers.remove(eventHandler);
        }
    }

    @Override
    public void Publish(Object sender, IEventArgs eventArgs)
    {
        for (IEventHandler eventHandler: _eventHandlers)
        {
            eventHandler.HandleEvent(sender, eventArgs);
        }
    }
}
