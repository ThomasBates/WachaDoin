package ca.turbobutterfly.core.data;

import java.util.HashMap;

public class DataRow implements IDataRow
{
    private IDataTable _table;

    private HashMap<IDataColumn, Object> _values = new HashMap<>();

    DataRow(IDataTable dataTable)
    {
        _table = dataTable;
    }

    @Override
    public IDataTable DataTable()
    {
        return _table;
    }

    @Override
    public Object Value(IDataColumn column)
    {
        if (column == null)
        {
            return null;
        }

        if (!_values.containsKey(column))
        {
            return null;
        }

        return _values.get(column);
    }

    @Override
    public Object Value(String columnName)
    {
        IDataColumn column = _table.Column(columnName);
        return Value(column);
    }

    @Override
    public Object Value(int columnIndex)
    {
        IDataColumn column = _table.Column(columnIndex);
        return Value(column);
    }

    @Override
    public void Value(IDataColumn column, Object value)
    {
        if (column == null)
        {
            return;
        }

        _values.put(column, value);
    }

    @Override
    public void Value(String columnName, Object value)
    {
        IDataColumn column = _table.Column(columnName);
        Value(column, value);
    }

    @Override
    public void Value(int columnIndex, Object value)
    {
        IDataColumn column = _table.Column(columnIndex);
        Value(column, value);
    }
}
