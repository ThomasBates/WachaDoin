package ca.turbobutterfly.core.options;

public class OptionsBooleanItem extends OptionsItem<Boolean>
{
    public OptionsBooleanItem(IOptionsGroup group, String name, Boolean defaultValue)
    {
        super(group, name, defaultValue);
    }

    @Override
    protected void ReadValue(IOptionsProvider provider, String name, Boolean defaultValue)
    {
        Boolean value = provider.ReadBoolean(name, defaultValue);
        Value(value);
    }

    @Override
    protected void WriteValue(IOptionsProvider provider, String name, Boolean value)
    {
        provider.WriteBoolean(name, value);
    }
}
