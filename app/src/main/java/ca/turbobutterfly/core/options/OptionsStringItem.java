package ca.turbobutterfly.core.options;

public class OptionsStringItem extends OptionsItem<String>
{
    public OptionsStringItem(IOptionsGroup group, String name, String defaultValue)
    {
        super(group, name, defaultValue);
    }

    @Override
    protected void ReadValue(IOptionsProvider provider, String name, String defaultValue)
    {
        String value = provider.ReadString(name, defaultValue);
        Value(value);
    }

    @Override
    protected void WriteValue(IOptionsProvider provider, String name, String value)
    {
        provider.WriteString(name, value);
    }
}
