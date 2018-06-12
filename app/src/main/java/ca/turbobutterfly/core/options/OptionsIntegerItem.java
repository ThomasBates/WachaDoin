package ca.turbobutterfly.core.options;

import java.text.ParseException;

public class OptionsIntegerItem extends OptionsItem<Integer>
{
    private boolean _storeAsString;

    public OptionsIntegerItem(IOptionsGroup group, String name, Integer defaultValue)
    {
        super(group, name, defaultValue);
    }

    public OptionsIntegerItem(IOptionsGroup group, String name, Integer defaultValue, boolean storeAsString)
    {
        super(group, name, defaultValue);

        _storeAsString = storeAsString;
    }

    @Override
    protected void ReadValue(IOptionsProvider provider, String name, Integer defaultValue)
    {
        if (_storeAsString)
        {
            String value = provider.ReadString(name, defaultValue.toString());
            try
            {
                Value(Integer.parseInt(value));
            }
            catch (NumberFormatException ex)
            {
                Value(defaultValue);
            }
        }
        else
        {
            Value(provider.ReadInteger(name, defaultValue));
        }
    }

    @Override
    protected void WriteValue(IOptionsProvider provider, String name, Integer value)
    {
        if (_storeAsString)
        {
            provider.WriteString(name, value.toString());
        }
        else
        {
            provider.WriteInteger(name, value);
        }
    }
}
