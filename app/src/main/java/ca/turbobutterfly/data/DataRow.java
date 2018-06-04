package ca.turbobutterfly.data;

import java.util.ArrayList;

class DataRow implements IDataRow
{
    //
    private ArrayList<String> _columns;
    private int _id;

    private ArrayList<String> _values;

    DataRow(ArrayList<String> columns, int id)
    {
        _columns = columns;
        _id = id;

        _values = new ArrayList<>();
        for (int i = 0; i < _columns.size(); i++)
        {
            _values.add(null);
        }
    }

    @Override
    public int ID()
    {
        return _id;
    }

    @Override
    public String Value(String columnName)
    {
        return _values.get(_columns.indexOf(columnName));
    }

    @Override
    public String Value(int columnIndex)
    {
        return _values.get(columnIndex);
    }

    @Override
    public void Value(String columnName, String value)
    {
        _values.set(_columns.indexOf(columnName), value);
    }

    @Override
    public void Value(int columnIndex, String value)
    {
        _values.set(columnIndex, value);
    }

    @Override
    public int ColumnCount()
    {
        return _columns.size();
    }
}
