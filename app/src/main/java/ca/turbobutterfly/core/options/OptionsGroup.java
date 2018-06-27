package ca.turbobutterfly.core.options;

import java.util.ArrayList;
import java.util.List;

import ca.turbobutterfly.core.events.Event;
import ca.turbobutterfly.core.events.IEvent;

public class OptionsGroup implements IOptionsGroup
{
    //  Variables ----------------------------------------------------------------------------------

    private IOptionsGroup _group;
    private String _name;

    private IOptionsProvider _provider;
    private String _nameSeparator;

    private IEvent _valueChanged = new Event();

    private List<IOptionsGroup> _groups = new ArrayList<>();
    private List<IOptionsItem> _items = new ArrayList<>();

    //  Constructors -------------------------------------------------------------------------------

    public OptionsGroup(IOptionsGroup group, String name)
    {
        _group = group;
        _name = name;

        if (_group != null)
        {
            _provider = _group.Provider();
            _nameSeparator = _provider.NameSeparator();

            _group.Groups().add(this);
        }
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
        return _group;
    }

    @Override
    public String Name()
    {
        return _name;
    }

    @Override
    public String FullName()
    {
        if (_group == null)
        {
            return _name;
        }

        return _group.FullName() + _nameSeparator + _name;
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

        if (_group != null)
        {
            _group.NotifyValueChanged(name);
        }
    }
}
