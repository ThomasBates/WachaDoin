package ca.turbobutterfly.core.grid;

import ca.turbobutterfly.core.data.IDataRow;

public class GridDataGroupRow implements IGridDataRow
{
    private IGridDataSource _dataSource;
    private IDataRow _dataRow;

    GridDataGroupRow(IGridDataSource dataSource, IDataRow row)
    {
        _dataSource = dataSource;
        _dataRow = row;
    }

    public IGridDataSource DataSource()
    {
        return _dataSource;
    }

    public IDataRow DataRow()
    {
        return _dataRow;
    }

    public Object Value(IGridDataColumn column)
    {
        return _dataRow.Value(column.DataColumn());
    }

    public Object Value(String columnName)
    {
        return _dataRow.Value(columnName);
    }

    public Object Value(int columnIndex)
    {
        return _dataRow.Value(columnIndex);
    }

    public boolean IsGroupRow()
    {
        return true;
    }
}
