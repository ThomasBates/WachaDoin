package ca.turbobutterfly.core.data;

public interface IDataRow
{
    IDataTable DataTable();

    Object Value(IDataColumn column);
    Object Value(String columnName);
    Object Value(int columnIndex);
    void Value(IDataColumn column, Object value);
    void Value(String columnName, Object value);
    void Value(int columnIndex, Object value);
}
