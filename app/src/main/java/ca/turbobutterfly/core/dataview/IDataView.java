package ca.turbobutterfly.core.dataview;

import java.util.List;

public interface IDataView
{
    List<IDataViewColumn> Columns();

    IDataViewColumn Column(String columnName);
    IDataViewColumn Column(int columnIndex);

    int GetColumnIndex(IDataViewColumn column);
    int GetColumnIndex(String columnName);
    String GetColumnName(int columnIndex);
    int ColumnCount();

    IDataViewRow GetRow(int rowIndex);
    int RowCount();

    String[] IndexFields();
    void IndexFields(String[] indexFields);

    String[] FilterFields();
    void FilterFields(String[] filterFields);

    Object[] FilterValues();
    void FilterValues(Object[] filterValues);
}
