package ca.turbobutterfly.core.grid;

import ca.turbobutterfly.core.data.IDataRow;

public interface IGridDataRow
{
    IGridDataSource DataSource();
    IDataRow DataRow();

    Object Value(IGridDataColumn column);
    Object Value(String columnName);
    Object Value(int columnIndex);

    boolean IsGroupRow();
}
