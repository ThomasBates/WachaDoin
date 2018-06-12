package ca.turbobutterfly.core.data;

public interface IDataReader
{
    boolean Read();
    void Close();

    String getString(String columnName);
    String getString(int columnIndex);

    //  TODO: Expand this as needed.
}
