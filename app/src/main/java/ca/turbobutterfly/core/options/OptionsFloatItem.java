package ca.turbobutterfly.core.options;

public class OptionsFloatItem extends OptionsItem<Float>
{
    public OptionsFloatItem(IOptionsGroup group, String name, Float defaultValue)
    {
        super(group, name, defaultValue);
    }

    @Override
    protected void ReadValue(IOptionsProvider provider, String name, Float defaultValue)
    {
        Value(provider.ReadFloat(name, defaultValue));
    }

    @Override
    protected void WriteValue(IOptionsProvider provider, String name, Float value)
    {
        provider.WriteFloat(name, value);
    }
}
