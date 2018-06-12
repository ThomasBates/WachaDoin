package ca.turbobutterfly.core.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataTable implements IDataTable
{
    private List<IDataColumn> _columns = new ArrayList<>();
    private HashMap<String, IDataColumn> _columnsByName = new HashMap<>();

    //private ArrayList<String> _columns = new ArrayList<>();
    private ArrayList<IDataRow> _rows = new ArrayList<>();

    @Override
    public List<IDataColumn> Columns()
    {
        return new ArrayList<>(_columns);
    }

    @Override
    public void Columns(List<IDataColumn> columns)
    {
        _columns = new ArrayList<>(columns);

        _columnsByName.clear();
        for (IDataColumn column : _columns)
        {
            _columnsByName.put(column.Name(), column);
        }
    }

    @Override
    public void AddColumn(IDataColumn column)
    {
        if (_columnsByName.containsKey(column.Name()))
        {
            return;
        }

        _columns.add(column);

        _columnsByName.put(column.Name(), column);
    }

    @Override
    public IDataColumn Column(String columnName)
    {
        if (!_columnsByName.containsKey(columnName))
        {
            return null;
        }

        return _columnsByName.get(columnName);
    }

    @Override
    public IDataColumn Column(int columnIndex)
    {
        return _columns.get(columnIndex);
    }

    @Override
    public int GetColumnIndex(IDataColumn column)
    {
        return _columns.indexOf(column);
    }

    @Override
    public int GetColumnIndex(String columnName)
    {
        IDataColumn column = Column(columnName);
        return _columns.indexOf(column);
    }

    @Override
    public String GetColumnName(int columnIndex)
    {
        IDataColumn column = _columns.get(columnIndex);
        return column.Name();
    }

    @Override
    public int ColumnCount()
    {
        return _columns.size();
    }


    @Override
    public IDataRow NewRow()
    {
        return new DataRow(this);
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
