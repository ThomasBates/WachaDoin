package ca.turbobutterfly.core.data;

public interface IDataSet
{
    IDataTable NewTable();
    void AddTable(IDataTable table);
    IDataTable GetTable(int tableIndex);
    void RemoveTable(int tableIndex);
    void ClearTables();
    int TableCount();
}
