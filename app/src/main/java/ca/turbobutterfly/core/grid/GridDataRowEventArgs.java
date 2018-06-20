package ca.turbobutterfly.core.grid;

public class GridDataRowEventArgs implements IGridDataRowEventArgs
{
    private final IGridDataRow _row;
    private final IGridDataColumn _column;

    public GridDataRowEventArgs(IGridDataRow row)
    {
        _row = row;
        _column = null;
    }

    public GridDataRowEventArgs(IGridDataRow row, IGridDataColumn column)
    {
        _row = row;
        _column = column;
    }

    public IGridDataRow Row()
    {
        return _row;
    }

    public IGridDataColumn Column()
    {
        return _column;
    }
}
