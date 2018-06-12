package ca.turbobutterfly.android.utils;

import android.os.Handler;

import ca.turbobutterfly.core.events.Event;
import ca.turbobutterfly.core.events.EventArgs;
import ca.turbobutterfly.core.events.IEvent;
import ca.turbobutterfly.core.utils.ITimer;

public class RunnableTimer implements ITimer
{
    //  Variables ----------------------------------------------------------------------------------

    private long _period = 1000;
    private boolean _active = false;

    private IEvent _onTick = new Event();

    //  runs without a timer by reposting this handler at the end of the runnable
    private Handler _timerHandler = new Handler();
    private Runnable _timerRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            _onTick.Publish(this, new EventArgs());

            _timerHandler.postDelayed(this, _period);
        }
    };

    //  Constructors -------------------------------------------------------------------------------

    public RunnableTimer()
    {
    }

    public RunnableTimer(long period)
    {
        _period = period;
    }

    public RunnableTimer(long period, boolean active)
    {
        _period = period;
        _active = active;
    }

    //  Events -------------------------------------------------------------------------------------

    @Override
    public IEvent OnTick()
    {
        return _onTick;
    }

    //  Properties ---------------------------------------------------------------------------------

    @Override
    public long Period()
    {
        return _period;
    }

    @Override
    public void Period(long period)
    {
        _period = period;
    }

    @Override
    public boolean Active()
    {
        return _active;
    }

    @Override
    public void Active(boolean active)
    {
        if (_active == active)
        {
            return;
        }

        _active = active;

        if (_active)
        {
            _timerHandler.postDelayed(_timerRunnable, 0);
        }
        else
        {
            _timerHandler.removeCallbacks(_timerRunnable);
        }
    }
}
