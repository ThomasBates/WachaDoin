package ca.turbobutterfly.core.dataview;

import ca.turbobutterfly.core.data.IDataRow;

public class DataViewRow implements IDataViewRow
{
    private IDataView _dataView;
    private IDataRow _dataRow;

    DataViewRow(IDataView dataView, IDataRow row)
    {
        _dataView = dataView;
        _dataRow = row;
    }

    @Override
    public IDataView DataView()
    {
        return _dataView;
    }

    @Override
    public IDataRow DataRow()
    {
        return _dataRow;
    }

    @Override
    public Object Value(IDataViewColumn column)
    {
        return _dataRow.Value(column.DataColumn());
    }

    @Override
    public Object Value(String columnName)
    {
        return _dataRow.Value(columnName);
    }

    @Override
    public Object Value(int columnIndex)
    {
        return _dataRow.Value(columnIndex);
    }
}
