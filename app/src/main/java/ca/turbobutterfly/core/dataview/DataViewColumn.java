package ca.turbobutterfly.core.dataview;

import ca.turbobutterfly.core.data.IDataColumn;

public class DataViewColumn implements IDataViewColumn
{
    private IDataColumn _column;

    public DataViewColumn(IDataColumn column)
    {
        _column = column;
    }

    @Override
    public IDataColumn DataColumn()
    {
        return _column;
    }

    @Override
    public String Name()
    {
        return _column.Name();
    }
}
