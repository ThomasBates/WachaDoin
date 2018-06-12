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

//    @Override
//    public String[] GetGroupNames()
//    {
//        List<String> names = new ArrayList<>();
//        for (IOptionsGroup group : _groups)
//        {
//            names.add(group.Name());
//        }
//        Collections.sort(names);
//        return names.toArray(new String[0]);
//    }
//
//    @Override
//    public String[] GetItemNames()
//    {
//        List<String> names = new ArrayList<>();
//        for (IOptionsItem item : _items)
//        {
//            names.add(item.Name());
//        }
//        Collections.sort(names);
//        return names.toArray(new String[0]);
//    }
//
//    @Override
//    public String[] GetChildNames()
//    {
//        return _provider.GetChildNames(Name());
//    }
//
//    @Override
//    public String[] GetChildNames(String groupPath)
//    {
//        return _provider.GetChildNames(Name() + "\\" + groupPath);
//    }
//
//    @Override
//    public void DoValueChanged(String name, String value)
//    {
//        OnValueChanged(new ValueChangedEventArgs(name, value));
//    }

    @Override
    public void NotifyValueChanged(String name)
    {
        _valueChanged.Publish(this, new ValueChangedEventArgs(name));
    }

//    @Override
//    public <T> void Write(String name, T value)
//    {
//        _provider.Write(_name + "\\" + name, value);
//    }
//
//    @Override
//    public <T> T Read(String name, T defaultValue)
//    {
//        return _provider.Read(_name + "\\" + name, defaultValue);
//    }

//    @Override
//    public void WriteString(String name, String value)
//    {
//        _provider.WriteString(_name + "\\" + name, value);
//    }
//
//    @Override
//    public String ReadString(String name)
//    {
//        return _provider.ReadString(_name + "\\" + name);
//    }
//
//    @Override
//    public void LoadAll()
//    {
//        for (IOptionsGroup group : _groups)
//        {
//            group.LoadAll();
//        }
//        for (IOptionsItem item : _items)
//        {
//            item.SetValueFromString();
//        }
//    }
//
//    @Override
//    public void SaveAll()
//    {
//        for (IOptionsGroup group : _groups)
//        {
//            group.SaveAll();
//        }
//        for (IOptionsItem item : _items)
//        {
//            item.SetStringFromValue();
//        }
//    }
//
//    @Override
//    public void RestoreDefaults()
//    {
//        for (IOptionsGroup group : _groups)
//        {
//            group.RestoreDefaults();
//        }
//        for (IOptionsItem item : _items)
//        {
//            item.RestoreDefaults();
//        }
//    }
//
//    @Override
//    public void Assign(IOptionsGroup source)
//    {
//        Delete();
//        String[] childNames = source.GetChildNames();
//
//        for (String childName : childNames)
//        {
//            IOptionsItem thisChildItem = new OptionsItem(this, childName);
//            IOptionsItem sourceChildItem = new OptionsItem(source, childName);
//
//            thisChildItem.AsString(sourceChildItem.AsString());
//
//            this.Items().remove(thisChildItem);
//            source.Items().remove(sourceChildItem);
//        }
//        for (String childName : childNames)
//        {
//            IOptionsGroup thisChildGroup = new OptionsGroup(this, childName);
//            IOptionsGroup sourceChildGroup = new OptionsGroup(source, childName);
//
//            thisChildGroup.Assign(sourceChildGroup);
//
//            this.Groups().remove(thisChildGroup);
//            source.Groups().remove(sourceChildGroup);
//        }
//    }
//
//    @Override
//    public void Delete()
//    {
//        _provider.Delete(_name);
//    }
//
//    @Override
//    public void Delete(String name)
//    {
//        _provider.Delete(_name + "\\" + name);
//    }

    //  Private Methods ----------------------------------------------------------------------------

    protected void OnValueChanged(ValueChangedEventArgs eventArgs)
    {
        _valueChanged.Publish(this, eventArgs);
    }
}
