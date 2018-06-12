package ca.turbobutterfly.core.grid;

import ca.turbobutterfly.core.data.IDataColumn;

public class GridDataColumn implements IGridDataColumn
{
    private IDataColumn _column;

    public GridDataColumn(IDataColumn column)
    {
        _column = column;
    }

    public IDataColumn DataColumn()
    {
        return _column;
    }

    public String Name()
    {
        return _column.Name();
    }
}
