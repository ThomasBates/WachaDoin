package ca.turbobutterfly.core.dataview;

import ca.turbobutterfly.core.data.IDataRow;

public interface IDataViewRow
{
    IDataView DataView();
    IDataRow DataRow();

    Object Value(IDataViewColumn column);
    Object Value(String columnName);
    Object Value(int columnIndex);
}
