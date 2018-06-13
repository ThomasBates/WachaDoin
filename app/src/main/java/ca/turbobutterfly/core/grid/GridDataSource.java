package ca.turbobutterfly.core.grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import ca.turbobutterfly.core.data.IDataColumn;
import ca.turbobutterfly.core.data.IDataRow;
import ca.turbobutterfly.core.data.IDataTable;

public class GridDataSource implements IGridDataSource
{
    //  Variables ----------------------------------------------------------------------------------

    private IDataTable _dataTable;

    private List<IGridDataColumn> _columns;
    private HashMap<String, IGridDataColumn> _columnsByName = new HashMap<>();

    private String[] _groupFields = new String[0];
    private String[] _indexFields = new String[0];
    private String[] _filterFields = new String[0];
    private Object[] _filterValues = new String[0];

    private List<IGridDataRow> _index;

    //  Constructors -------------------------------------------------------------------------------

    public GridDataSource(IDataTable dataTable)
    {
        _dataTable = dataTable;

        _columns = new ArrayList<>();

        for (IDataColumn column : _dataTable.Columns())
        {
            IGridDataColumn gridDataColumn = new GridDataColumn(column);
            _columns.add(gridDataColumn);
        }
    }

    //  Methods ------------------------------------------------------------------------------------

    public List<IGridDataColumn> Columns()
    {
        return _columns;
    }

    public IGridDataColumn Column(String columnName)
    {
        if (!_columnsByName.containsKey(columnName))
        {
            return null;
        }

        return _columnsByName.get(columnName);
    }

    public IGridDataColumn Column(int columnIndex)
    {
        return _columns.get(columnIndex);
    }

    public int GetColumnIndex(IGridDataColumn column)
    {
        return _columns.indexOf(column);
    }

    public int GetColumnIndex(String columnName)
    {
        IGridDataColumn column = Column(columnName);
        return _columns.indexOf(column);
    }

    public String GetColumnName(int columnIndex)
    {
        IGridDataColumn column = Column(columnIndex);
        return column.Name();
    }

    public int ColumnCount()
    {
        return _columns.size();
    }

    public IGridDataRow GetRow(int rowIndex)
    {
        CreateIndex();

        if (_index == null)
        {
            return null;
        }

        return _index.get(rowIndex);
    }

    public int RowCount()
    {
        CreateIndex();

        if (_index == null)
        {
            return 0;
        }

        return _index.size();
    }

    public String[] GroupFields()
    {
        return _groupFields;
    }

    public void GroupFields(String[] groupFields)
    {
        _groupFields = groupFields;

        if (_groupFields == null)
        {
            _groupFields = new String[0];
        }

        InvalidateIndex();
    }

    public String[] IndexFields()
    {
        return _indexFields;
    }

    public void IndexFields(String[] indexFields)
    {
        _indexFields = indexFields;

        if (_indexFields == null)
        {
            _indexFields = new String[0];
        }

        InvalidateIndex();
    }

    public String[] FilterFields()
    {
        return _filterFields;
    }

    public void FilterFields(String[] filterFields)
    {
        _filterFields = filterFields;

        if (_filterFields == null)
        {
            _filterFields = new String[0];
        }

        InvalidateIndex();
    }

    public Object[] FilterValues()
    {
        return _filterValues;
    }

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
        if (_index != null)
        {
            return;
        }

        _index = new ArrayList<>();

        FilterRows();
        SortRows();
        GroupRows();
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

                if (rowValue == null ? filterValue != null : !rowValue.equals(filterValue))
                {
                    filterOk = false;
                }
            }

            if (filterOk)
            {
                _index.add(new GridDataRow(this, row));
            }
        }
    }

    private void SortRows()
    {
        Collections.sort(_index, new Comparator<IGridDataRow>()
        {
            @Override
            public int compare(IGridDataRow lhs, IGridDataRow rhs)
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

                    int compare = lhsValue.compareToIgnoreCase(rhsValue);
                    if (compare != 0)
                    {
                        return compare * direction;
                    }
                }

                return 0;
            }
        });
    }

    private void GroupRows()
    {
        if (_groupFields.length == 0)
        {
            return;
        }

        Object[] groupValues = new Object[_groupFields.length];

        int rowIndex = 0;
        while (rowIndex < _index.size())
        {
            IGridDataRow row = _index.get(rowIndex);

            boolean addGroup = (rowIndex == 0);

            if (!addGroup)
            {
                for (int i = 0; i < _groupFields.length; i++)
                {
                    Object rowValue = row.Value(_groupFields[i]);
                    Object groupValue = groupValues[i];

                    if (rowValue == null ? groupValue != null : !rowValue.equals(groupValue))
                    {
                        addGroup = true;
                        break;
                    }
                }
            }

            if (addGroup)
            {
                IGridDataRow groupRow = new GridDataGroupRow(this, row.DataRow());
                _index.add(rowIndex, groupRow);

                for (int i = 0; i < _groupFields.length; i++)
                {
                    groupValues[i] = row.Value(_groupFields[i]);
                }

                rowIndex++;
            }

            rowIndex++;
        }
    }
}
