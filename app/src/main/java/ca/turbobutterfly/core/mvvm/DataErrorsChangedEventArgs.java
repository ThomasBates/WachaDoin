package ca.turbobutterfly.core.mvvm;

public class DataErrorsChangedEventArgs implements IDataErrorsChangedEventArgs
{
    private String _propertyName;

    public DataErrorsChangedEventArgs(String propertyName)
    {
        _propertyName = propertyName;
    }

    @Override
    public String PropertyName()
    {
        return _propertyName;
    }
}
