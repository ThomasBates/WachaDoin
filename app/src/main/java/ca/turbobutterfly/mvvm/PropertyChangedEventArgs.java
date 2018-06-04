package ca.turbobutterfly.mvvm;

public class PropertyChangedEventArgs implements IPropertyChangedEventArgs
{
    private String _propertyName;

    public PropertyChangedEventArgs(String propertyName)
    {
        _propertyName = propertyName;
    }

    @Override
    public String PropertyName()
    {
        return _propertyName;
    }
}
