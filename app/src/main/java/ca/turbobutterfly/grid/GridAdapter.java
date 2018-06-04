package ca.turbobutterfly.grid;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import ca.turbobutterfly.data.IDataRow;
import ca.turbobutterfly.data.IDataTable;

public class GridAdapter extends BaseAdapter
{
    private Context _context;
    private IDataTable _dataSource;
    private ArrayList<GridColumn> _gridColumns;

    GridAdapter(Context context, IDataTable dataSource, ArrayList<GridColumn> gridColumns)
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
        GridRowView rowView;

        IDataRow row = _dataSource.GetRow(position);

        if (convertView == null)
        {
            rowView = new GridRowView(_context, _gridColumns, row);
        }
        else
        {
            rowView = (GridRowView)convertView;
            rowView.Populate(row);
        }
        return rowView;
    }
}
