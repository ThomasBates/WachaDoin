package ca.turbobutterfly.core.options;

import java.util.Date;

public class OptionsDateItem extends OptionsItem<Date>
{
    public OptionsDateItem(IOptionsGroup group, String name, Date defaultValue)
    {
        super(group, name, defaultValue);
    }

    @Override
    protected void ReadValue(IOptionsProvider provider, String name, Date defaultValue)
    {
        Value(provider.ReadDate(name, defaultValue));
    }

    @Override
    protected void WriteValue(IOptionsProvider provider, String name, Date value)
    {
        provider.WriteDate(name, value);
    }
}
