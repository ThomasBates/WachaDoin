package ca.turbobutterfly.core.options;

import ca.turbobutterfly.core.events.EventArgs;

public class ValueChangedEventArgs extends EventArgs
{
    private String _name;

    public ValueChangedEventArgs(String name)
    {
        _name = name;
    }

    public String Name()
    {
        return _name;
    }
}
