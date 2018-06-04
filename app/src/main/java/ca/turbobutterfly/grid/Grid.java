package ca.turbobutterfly.grid;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import ca.turbobutterfly.data.IDataTable;

public class Grid extends LinearLayout
{
    private Context _context;

    private ArrayList<GridColumn> _gridColumns;
    private IDataTable _dataSource;
    private ListView _listView;
    private GridAdapter _adapter;
    private String _noDataText;
    private LinearLayout _gridHeader;

    public Grid(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        _context = context;

        setOrientation(VERTICAL);

        _gridColumns = new ArrayList<>();
    }

    public void AddGridColumn(GridColumn gridColumn)
    {
        _gridColumns.add(gridColumn);
    }

    public void SetNoDataText(String noDataText)
    {
        _noDataText = noDataText;
    }

    public void Refresh()
    {
        if (_gridHeader == null)
        {
            Create();
        }
    }

    public void Create()
    {
        _gridHeader = new GridHeaderView(_context, _gridColumns);
        _listView = new ListView(_context);
        _listView.setDividerHeight(2);

        if (_dataSource != null)
        {
            _adapter = new GridAdapter(_context, _dataSource, _gridColumns);
            _listView.setAdapter(_adapter);
        }

        addView(_gridHeader);
        addView(_listView);
    }

    public void SetDataSource(IDataTable dataSource)
    {
        _dataSource = dataSource;

        if (_listView != null)
        {
            _adapter = new GridAdapter(_context, _dataSource, _gridColumns);
            _listView.setAdapter(_adapter);
        }
    }
}
