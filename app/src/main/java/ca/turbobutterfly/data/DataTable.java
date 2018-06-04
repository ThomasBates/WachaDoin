package ca.turbobutterfly.data;

import java.util.ArrayList;
import java.util.Arrays;

public class DataTable implements IDataTable
{
    private ArrayList<String> _columns = new ArrayList<>();
    private ArrayList<IDataRow> _rows = new ArrayList<>();
    private int _indexCounter = 0;

    @Override
    public void AddColumns(String[] columns)
    {
        _columns.addAll(Arrays.asList(columns));
    }

    @Override
    public int ColumnCount()
    {
        return _columns.size();
    }

    @Override
    public int GetColumnIndex(String columnName)
    {
        return _columns.indexOf(columnName);
    }

    @Override
    public String GetColumnName(int columnIndex)
    {
        return _columns.get(columnIndex);
    }

    @Override
    public IDataRow NewRow()
    {
        IDataRow row = new DataRow(_columns, _indexCounter);
        _indexCounter++;
        return row;
    }

    @Override
    public void AddRow(IDataRow row)
    {
        _rows.add(row);
    }

    @Override
    public IDataRow GetRow(int rowIndex)
    {
        return _rows.get(rowIndex);
    }

    @Override
    public void RemoveRow(int rowIndex)
    {
        _rows.remove(rowIndex);
    }

    @Override
    public void ClearRows()
    {
        _rows.clear();
    }

    @Override
    public int RowCount()
    {
        return _rows.size();
    }
}
