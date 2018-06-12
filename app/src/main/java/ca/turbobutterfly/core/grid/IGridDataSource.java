package ca.turbobutterfly.core.grid;

import java.util.List;

public interface IGridDataSource
{
    List<IGridDataColumn> Columns();

    IGridDataColumn Column(String columnName);
    IGridDataColumn Column(int columnIndex);

    int GetColumnIndex(IGridDataColumn column);
    int GetColumnIndex(String columnName);
    String GetColumnName(int columnIndex);
    int ColumnCount();

    IGridDataRow GetRow(int rowIndex);
    int RowCount();

    String[] GroupFields();
    void GroupFields(String[] groupFields);

    String[] IndexFields();
    void IndexFields(String[] indexFields);

    String[] FilterFields();
    void FilterFields(String[] filterFields);

    Object[] FilterValues();
    void FilterValues(Object[] filterValues);
}
