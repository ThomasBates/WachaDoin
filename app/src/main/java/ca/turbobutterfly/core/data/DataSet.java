package ca.turbobutterfly.core.data;

import java.util.ArrayList;

public class DataSet implements IDataSet
{
    private ArrayList<IDataTable> _tables = new ArrayList<>();

    @Override
    public IDataTable NewTable()
    {
        return new DataTable();
    }

    @Override
    public void AddTable(IDataTable table)
    {
        _tables.add(table);
    }

    @Override
    public IDataTable GetTable(int tableIndex)
    {
        return _tables.get(tableIndex);
    }

    @Override
    public void RemoveTable(int tableIndex)
    {
        _tables.remove(tableIndex);
    }

    @Override
    public void ClearTables()
    {
        _tables.clear();
    }

    @Override
    public int TableCount()
    {
        return _tables.size();
    }
}
