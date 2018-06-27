package ca.turbobutterfly.core.options;

import java.util.ArrayList;
import java.util.List;

import ca.turbobutterfly.core.events.Event;
import ca.turbobutterfly.core.events.IEvent;

public class OptionsRoot implements IOptionsGroup
{
    //  Variables ----------------------------------------------------------------------------------

    private IOptionsProvider _provider;
    private String _name;

    private IEvent _valueChanged = new Event();

    private List<IOptionsGroup> _groups = new ArrayList<>();
    private List<IOptionsItem> _items = new ArrayList<>();

    //  Constructors -------------------------------------------------------------------------------

    public OptionsRoot(IOptionsProvider provider, String name)
    {
        _provider = provider;
        _name = name;
    }

    //  Events -------------------------------------------------------------------------------------

    @Override
    public IEvent ValueChanged()
    {
        return _valueChanged;
    }

    //  Properties ---------------------------------------------------------------------------------


    @Override
    public IOptionsProvider Provider()
    {
        return _provider;
    }

    @Override
    public IOptionsGroup Group()
    {
        return null;
    }

    @Override
    public String Name()
    {
        return _name;
    }

    @Override
    public String FullName()
    {
        return _name;
    }

    @Override
    public List<IOptionsGroup> Groups()
    {
        return _groups;
    }

    @Override
    public List<IOptionsItem> Items()
    {
        return _items;
    }

    //  Methods ------------------------------------------------------------------------------------

    @Override
    public void NotifyValueChanged(String name)
    {
        _valueChanged.Publish(this, new ValueChangedEventArgs(name));
    }

    //  Private Methods ----------------------------------------------------------------------------

    protected void OnValueChanged(ValueChangedEventArgs eventArgs)
    {
        _valueChanged.Publish(this, eventArgs);
    }
}
