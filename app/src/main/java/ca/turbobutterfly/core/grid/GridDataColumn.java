package ca.turbobutterfly.core.grid;

import ca.turbobutterfly.core.data.IDataColumn;

public class GridDataColumn implements IGridDataColumn
{
    private IDataColumn _dataColumn;

    public GridDataColumn(IDataColumn dataColumn)
    {
        _dataColumn = dataColumn;
    }

    public IDataColumn DataColumn()
    {
        return _dataColumn;
    }

    public String Name()
    {
        return _dataColumn.Name();
    }
}
