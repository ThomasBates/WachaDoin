package ca.turbobutterfly.data;

public interface IDataRow
{
    int ID();

    String Value(String columnName);
    String Value(int columnIndex);
    void Value(String columnName, String value);
    void Value(int columnIndex, String value);

    int ColumnCount();
}
