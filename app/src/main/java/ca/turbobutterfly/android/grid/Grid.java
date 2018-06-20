package ca.turbobutterfly.android.grid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import ca.turbobutterfly.core.events.Event;
import ca.turbobutterfly.core.events.IEvent;
import ca.turbobutterfly.core.grid.GridColumn;
import ca.turbobutterfly.core.grid.GridDataRowEventArgs;
import ca.turbobutterfly.core.grid.IGridDataColumn;
import ca.turbobutterfly.core.grid.IGridDataRow;
import ca.turbobutterfly.core.grid.IGridDataSource;
import ca.turbobutterfly.wachadoin.R;

public class Grid extends LinearLayout
{
    private Context _context;

    private final IEvent _onRowClick = new Event();
    private final IEvent _onCellLongClick = new Event();
    private final IEvent _onCellClick = new Event();

    private ArrayList<GridColumn> _gridColumns;
    private IGridDataSource _dataSource;

    private LinearLayout _gridHeader;
    private ListView _listView;
    private GridAdapter _adapter;

    private OnLongClickListener _onLongClickListener = new OnLongClickListener()
    {
        @Override
        public boolean onLongClick(View v)
        {
            if (_onCellLongClick.EventHandlerCount() == 0)
            {
                return false;
            }
            IGridDataRow row = (IGridDataRow)v.getTag(R.string.grid_row_tag);
            String columnName = (String)v.getTag(R.string.grid_column_name_tag);
            IGridDataColumn column = _dataSource.Column(columnName);

            _onCellLongClick.Publish(this, new GridDataRowEventArgs(row, column));
            return true;
        }
    };

    private OnClickListener _onClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (_onCellClick.EventHandlerCount() == 0)
            {
                return;
            }
            IGridDataRow row = (IGridDataRow)v.getTag(R.string.grid_row_tag);
            String columnName = (String)v.getTag(R.string.grid_column_name_tag);
            IGridDataColumn column = _dataSource.Column(columnName);

            _onCellClick.Publish(this, new GridDataRowEventArgs(row, column));
        }
    };

    public Grid(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        _context = context;

        setOrientation(VERTICAL);

        _gridColumns = new ArrayList<>();
    }

    public IEvent OnRowClick()
    {
        return _onRowClick;
    }

    public IEvent OnCellLongClick()
    {
        return _onCellLongClick;
    }

    public IEvent OnCellClick()
    {
        return _onCellClick;
    }

    public void AddGridColumn(String caption, String fieldName, int width)
    {
        GridColumn gridColumn = new GridColumn(caption, fieldName, width);
        _gridColumns.add(gridColumn);
    }

    public void AddGridColumn(GridColumn gridColumn)
    {
        _gridColumns.add(gridColumn);
    }

    public int GetTopPosition()
    {
        return _listView.getFirstVisiblePosition();
    }

    public void SetTopPosition(int top)
    {
        _listView.setSelection(top);
    }


    public void SetDataSource(IGridDataSource dataSource)
    {
        _dataSource = dataSource;

        SetupGrid();
    }

    private void SetupGrid()
    {
        removeAllViews();

        _gridHeader = new GridHeaderView(_context, _gridColumns, _dataSource);

        _adapter = new GridAdapter(
                _context,
                _dataSource,
                _gridColumns,
                _onLongClickListener,
                _onClickListener);

        _listView = new ListView(_context);
        _listView.setDividerHeight(2);
        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (_onRowClick.EventHandlerCount() == 0)
                {
                    return;
                }
                GridRowView rowView = (GridRowView) view;
                IGridDataRow row = rowView.Row();
                _onRowClick.Publish(this, new GridDataRowEventArgs(row));
            }
        });
        _listView.setAdapter(_adapter);

        addView(_gridHeader);
        addView(_listView);
    }
}
