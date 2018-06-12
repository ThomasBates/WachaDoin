package ca.turbobutterfly.core.utils;

import ca.turbobutterfly.core.events.IEvent;

public interface ITimer
{
    IEvent OnTick();

    long Period();
    void Period(long period);

    boolean Active();
    void Active(boolean active);
}
