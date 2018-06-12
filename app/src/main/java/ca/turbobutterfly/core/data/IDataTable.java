package ca.turbobutterfly.core.data;

import java.util.List;

public interface IDataTable
{
    List<IDataColumn> Columns();
    void Columns(List<IDataColumn> columns);

    void AddColumn(IDataColumn column);
    IDataColumn Column(String columnName);
    IDataColumn Column(int columnIndex);

    int GetColumnIndex(IDataColumn column);
    int GetColumnIndex(String columnName);
    String GetColumnName(int columnIndex);
    int ColumnCount();

    IDataRow NewRow();
    void AddRow(IDataRow row);
    IDataRow GetRow(int rowIndex);
    void RemoveRow(int rowIndex);
    void ClearRows();
    int RowCount();
}
