package ca.turbobutterfly.android.grid;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import ca.turbobutterfly.core.grid.GridColumn;
import ca.turbobutterfly.core.grid.IGridDataRow;
import ca.turbobutterfly.core.grid.IGridDataSource;

public class GridAdapter extends BaseAdapter
{
    private Context _context;
    private IGridDataSource _dataSource;
    private ArrayList<GridColumn> _gridColumns;

    private HashMap<IGridDataRow, GridRowView> _viewByRow = new HashMap<>();

    GridAdapter(
            Context context,
            IGridDataSource dataSource,
            ArrayList<GridColumn> gridColumns)
    {
        _context = context;
        _dataSource = dataSource;
        _gridColumns = gridColumns;
    }

    public int getCount()
    {
        return _dataSource.RowCount();
    }

    public Object getItem(int position)
    {
        return _dataSource.GetRow(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        IGridDataRow row = _dataSource.GetRow(position);

        GridRowView rowView = _viewByRow.get(row);

        if (rowView == null)
        {
            rowView = new GridRowView(_context, _gridColumns, row);
            _viewByRow.put(row, rowView);
        }

//        if (convertView == null)
//        {
//            rowView = new GridRowView(_context, _gridColumns, row);
//        }
//        else
//        {
//            rowView = (GridRowView)convertView;
//            rowView.Populate(row);
//        }

        return rowView;
    }
}
