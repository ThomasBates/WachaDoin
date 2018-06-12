package ca.turbobutterfly.core.data;

public class DataColumn implements IDataColumn
{
    private String _name;

    public DataColumn(String name)
    {
        _name = name;
    }

    @Override
    public String Name()
    {
        return _name;
    }
}
