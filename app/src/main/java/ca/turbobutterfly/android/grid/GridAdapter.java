package ca.turbobutterfly.android.grid;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import ca.turbobutterfly.core.events.IEvent;
import ca.turbobutterfly.core.grid.GridColumn;
import ca.turbobutterfly.core.grid.IGridDataRow;
import ca.turbobutterfly.core.grid.IGridDataSource;

public class GridAdapter extends BaseAdapter
{
    private final Context _context;
    private final IGridDataSource _dataSource;
    private final ArrayList<GridColumn> _gridColumns;
    private final View.OnLongClickListener _onLongClickListener;
    private final View.OnClickListener _onClickListener;

    private HashMap<IGridDataRow, GridRowView> _viewByRow = new HashMap<>();

    GridAdapter(
            Context context,
            IGridDataSource dataSource,
            ArrayList<GridColumn> gridColumns,
            View.OnLongClickListener onLongClickListener,
            View.OnClickListener onClickListener)
    {
        _context = context;
        _dataSource = dataSource;
        _gridColumns = gridColumns;
        _onLongClickListener = onLongClickListener;
        _onClickListener = onClickListener;
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
            rowView = new GridRowView(
                    _context,
                    _gridColumns,
                    row,
                    _onLongClickListener,
                    _onClickListener);
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
