package ca.turbobutterfly.events;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Event implements IEvent
{
    ArrayList<IEventHandler> _eventHandlers = new ArrayList<IEventHandler>();

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
