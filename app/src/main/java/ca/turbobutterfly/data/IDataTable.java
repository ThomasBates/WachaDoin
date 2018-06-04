package ca.turbobutterfly.data;

public interface IDataTable
{
    void AddColumns(String[] columns);
    int ColumnCount();
    int GetColumnIndex(String columnName);
    String GetColumnName(int columnIndex);

    IDataRow NewRow();
    void AddRow(IDataRow row);
    IDataRow GetRow(int rowIndex);
    void RemoveRow(int rowIndex);
    void ClearRows();
    int RowCount();
}
