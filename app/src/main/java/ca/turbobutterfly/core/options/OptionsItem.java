package ca.turbobutterfly.core.options;

import ca.turbobutterfly.core.events.Event;
import ca.turbobutterfly.core.events.IEvent;

class OptionsItem<T> implements IOptionsItem<T>
{
    //  Variables ----------------------------------------------------------------------------------

    private IOptionsGroup _group;
    private String _name;
    private T _value;
    private T _defaultValue;

    private IOptionsProvider _provider;
    private String _nameSeparator;

    private IEvent _valueChanged = new Event();

    //  Constructors -------------------------------------------------------------------------------

    public OptionsItem(IOptionsGroup group, String name, T defaultValue)
    {
        _group = group;
        _name = name;
        _value = defaultValue;
        _defaultValue = defaultValue;

        _provider = _group.Provider();
        _nameSeparator = _provider.NameSeparator();

        _group.Items().add(this);
    }

    //  Events -------------------------------------------------------------------------------------

    @Override
    public IEvent ValueChanged()
    {
        return _valueChanged;
    }

    //  Properties ---------------------------------------------------------------------------------

    @Override
    public IOptionsGroup Group()
    {
        return _group;
    }

    @Override
    public void Group(IOptionsGroup group)
    {
        _group = group;
    }

    @Override
    public String Name()
    {
        return _name;
    }

    @Override
    public void Name(String name)
    {
        _name = name;
    }

    @Override
    public String FullName()
    {
        return _group.FullName() + _nameSeparator + _name;
    }

    @Override
    public T Value()
    {
        ReadValue(_provider, FullName(), _defaultValue);
        return _value;
    }

    @Override
    public void Value(T value)
    {
        if (_value == null ? value == null : _value.equals(value))
        {
            return;
        }
        _value = value;
        //_group.Write(_name, _value);
        WriteValue(_provider, FullName(), _value);
        NotifyValueChanged();
    }

    @Override
    public T DefaultValue()
    {
        //_defaultValue = GetValueFromString(_defaultString, _defaultValue);
        return _defaultValue;
    }

    @Override
    public void DefaultValue(T defaultValue)
    {
        _defaultValue = defaultValue;
//        _defaultString = GetStringFromValue(_defaultValue);
//        _valueString = _defaultString;
    }
//
//    @Override
//    public String ValueString()
//    {
//        return _valueString;
//    }
//
//    @Override
//    public void ValueString(String valueString)
//    {
//        _valueString = valueString;
//    }
//
//    @Override
//    public String DefaultString()
//    {
//        return _defaultString;
//    }
//
//    @Override
//    public void DefaultString(String defaultString)
//    {
//        _defaultString = defaultString;
//    }
//
//    @Override
//    public boolean IsEmpty()
//    {
//        String temp = _group.ReadString(_name);
//        return (temp == null) || temp.isEmpty();
//    }
//
//    @Override
//    public String AsString()
//    {
//        SetValueFromString();
//        return _valueString;
//    }
//
//    @Override
//    public void AsString(String value)
//    {
//        if ((value != null) &&
//            (!value.isEmpty()) &&
//            (!value.equals(_valueString)))
//        {
//            _valueString = value;
//            SaveString();
//            NotifyValueChanged();
//            SetValueFromString();
//        }
//    }
//
//    @Override
//    public void SetValueFromString()
//    {
//        LoadString();
//        NotifyValueChanged();
//        _value = GetValueFromString(_valueString, _defaultValue);
//    }
//
//    @Override
//    public void SetStringFromValue()
//    {
//        _valueString = GetStringFromValue(_value);
//        SaveString();
//        NotifyValueChanged();
//    }
//
//    @Override
//    public T GetValueFromString(String value, T defaultValue)
//    {
//        return defaultValue;
//    }
//
//    @Override
//    public String GetStringFromValue(T value)
//    {
//        return null;
//    }
//
//    @Override
//    public void RestoreDefaults()
//    {
//        AsString(_defaultString);
//    }

    //  Private Methods ----------------------------------------------------------------------------

    private void NotifyValueChanged()
    {
        _valueChanged.Publish(this, new ValueChangedEventArgs(FullName()));
        _group.NotifyValueChanged(FullName());
    }

    protected void ReadValue(IOptionsProvider provider, String name, T defaultValue)
    {
    }

    protected void WriteValue(IOptionsProvider provider, String name, T value)
    {
    }
}
