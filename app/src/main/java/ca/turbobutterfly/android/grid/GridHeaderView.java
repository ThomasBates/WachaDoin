package ca.turbobutterfly.android.grid;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ca.turbobutterfly.core.grid.IGridDataSource;

public class GridHeaderView extends LinearLayout
{
    private Context _context;
    private ArrayList<GridColumn> _gridColumns;
    private IGridDataSource _dataSource;

    private TextView[] _textViews;

    public GridHeaderView(
            Context context,
            ArrayList<GridColumn> gridColumns,
            IGridDataSource dataSource)
    {
        super(context);

        _context = context;
        _gridColumns = gridColumns;
        _dataSource = dataSource;

        setOrientation(HORIZONTAL);

        List<String> groupFields = new ArrayList<>(Arrays.asList(dataSource.GroupFields()));

        _textViews = new TextView[_gridColumns.size()];

        for (int i = 0; i < _gridColumns.size(); i++)
        {
            GridColumn gridColumn = _gridColumns.get(i);

            if (groupFields.contains(gridColumn.FieldName()))
            {
                continue;
            }

            TextView textView = new TextView(_context);
            _textViews[i] = textView;
            textView.setTextSize(16);
            textView.setPadding(5, 0, 5, 0);
            textView.setBackgroundColor(0xFF3F51B5); // (Color.LTGRAY);
            textView.setTextColor(Color.WHITE); // (Color.BLACK);
            textView.setWidth(gridColumn.Width()); //  * _scale);
            textView.setText(gridColumn.Caption());
            textView.setSingleLine(true);
            textView.setGravity(Gravity.CENTER_VERTICAL);

            addView(textView, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            TextView splitterView = new TextView(_context);
            addView(splitterView, new LinearLayout.LayoutParams(
                    5,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }
}
