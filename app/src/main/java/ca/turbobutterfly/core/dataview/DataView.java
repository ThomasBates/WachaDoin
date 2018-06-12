package ca.turbobutterfly.core.dataview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import ca.turbobutterfly.core.data.IDataColumn;
import ca.turbobutterfly.core.data.IDataRow;
import ca.turbobutterfly.core.data.IDataTable;

public class DataView implements IDataView
{
    //  Variables ----------------------------------------------------------------------------------

    private IDataTable _dataTable;

    private List<IDataViewColumn> _columns;
    private HashMap<String, IDataViewColumn> _columnsByName = new HashMap<>();

    private String[] _indexFields = new String[0];
    private String[] _filterFields = new String[0];
    private Object[] _filterValues = new String[0];

    private List<IDataViewRow> _index;

    //  Constructors -------------------------------------------------------------------------------

    public DataView(IDataTable dataTable)
    {
        _dataTable = dataTable;

        _columns = new ArrayList<>();

        for (IDataColumn column : _dataTable.Columns())
        {
            IDataViewColumn dataViewColumn = new DataViewColumn(column);
            _columns.add(dataViewColumn);
            _columnsByName.put(column.Name(), dataViewColumn);
        }
    }

    //  Methods ------------------------------------------------------------------------------------

    @Override
    public List<IDataViewColumn> Columns()
    {
        return _columns;
    }

    @Override
    public IDataViewColumn Column(String columnName)
    {
        if (!_columnsByName.containsKey(columnName))
        {
            return null;
        }

        return _columnsByName.get(columnName);
    }

    @Override
    public IDataViewColumn Column(int columnIndex)
    {
        return _columns.get(columnIndex);
    }

    @Override
    public int GetColumnIndex(IDataViewColumn column)
    {
        return _columns.indexOf(column);
    }

    @Override
    public int GetColumnIndex(String columnName)
    {
        IDataViewColumn column = Column(columnName);
        return _columns.indexOf(column);
    }

    @Override
    public String GetColumnName(int columnIndex)
    {
        IDataViewColumn column = Column(columnIndex);
        return column.Name();
    }

    @Override
    public int ColumnCount()
    {
        return _columns.size();
    }

    @Override
    public IDataViewRow GetRow(int rowIndex)
    {
        CreateIndex();

        if (_index == null)
        {
            return null;
        }

        return _index.get(rowIndex);
    }

    @Override
    public int RowCount()
    {
        CreateIndex();

        if (_index == null)
        {
            return 0;
        }

        return _index.size();
    }

    @Override
    public String[] IndexFields()
    {
        return _indexFields;
    }

    @Override
    public void IndexFields(String[] indexFields)
    {
        _indexFields = indexFields;

        if (_indexFields == null)
        {
            _indexFields = new String[0];
        }

        InvalidateIndex();
    }

    @Override
    public String[] FilterFields()
    {
        return _filterFields;
    }

    @Override
    public void FilterFields(String[] filterFields)
    {
        _filterFields = filterFields;

        if (_filterFields == null)
        {
            _filterFields = new String[0];
        }

        InvalidateIndex();
    }

    @Override
    public Object[] FilterValues()
    {
        return _filterValues;
    }

    @Override
    public void FilterValues(Object[] filterValues)
    {
        _filterValues = filterValues;

        if (_filterValues == null)
        {
            _filterValues = new String[0];
        }

        InvalidateIndex();
    }

    private void InvalidateIndex()
    {
        _index = null;
    }

    private void CreateIndex()
    {
        //  If it is already created, we don't need to recreate it.
        if (_index != null)
        {
            return;
        }

        _index = new ArrayList<>();

        FilterRows();
        SortRows();
    }

    private void FilterRows()
    {
        int filterCount = Math.min(_filterFields.length, _filterValues.length);

        for (int rowIndex = 0; rowIndex < _dataTable.RowCount(); rowIndex++)
        {
            IDataRow row = _dataTable.GetRow(rowIndex);
            boolean filterOk = true;

            for (int filterIndex = 0; filterIndex < filterCount; filterIndex++)
            {
                Object rowValue = row.Value(_filterFields[filterIndex]);
                Object filterValue = _filterValues[filterIndex];

                if (rowValue == null ? filterValue != null : rowValue.equals(filterValue))
                {
                    filterOk = false;
                }
            }

            if (filterOk)
            {
                _index.add(new DataViewRow(this, row));
            }
        }
    }

    private void SortRows()
    {
        Collections.sort(_index, new Comparator<IDataViewRow>()
        {
            @Override
            public int compare(IDataViewRow lhs, IDataViewRow rhs)
            {
                for (String indexField : _indexFields)
                {
                    int direction = 1;
                    if (indexField.toUpperCase().endsWith(" DESC"))
                    {
                        direction = -1;
                        indexField = indexField.substring(0, indexField.length() - 5);
                    }

                    String lhsValue = lhs.Value(indexField).toString();
                    String rhsValue = rhs.Value(indexField).toString();

                    int compare = lhsValue.compareTo(rhsValue);
                    if (compare != 0)
                    {
                        return compare * direction;
                    }
                }

                return 0;
            }
        });
    }
}
