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

import ca.turbobutterfly.core.grid.GridColumn;
import ca.turbobutterfly.core.grid.IGridDataRow;
import ca.turbobutterfly.core.grid.IGridDataSource;

public class GridRowView extends LinearLayout
{
    Context _context;
    public List<GridColumn> _gridColumns;

    TextView[] _textViews;

    public GridRowView(
            Context context,
            ArrayList<GridColumn> gridColumns,
            IGridDataRow row)
    {
        super(context);

        _context = context;
        _gridColumns = gridColumns;

        setOrientation(HORIZONTAL);

        IGridDataSource dataSource = row.DataSource();
        List<String> groupFields = new ArrayList<>(Arrays.asList(dataSource.GroupFields()));

        if (row.IsGroupRow())
        {
            _textViews = new TextView[1];

            TextView textView = new TextView(_context);
            _textViews[0] = textView;

            textView.setPadding(5, 0, 5, 0);
            textView.setBackgroundColor(Color.LTGRAY); // (0xFFEEEEEE);
            textView.setTextColor(Color.BLACK);
            textView.setSingleLine(true);
            textView.setGravity(Gravity.CENTER_VERTICAL);

            addView(textView, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        else
        {
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

                textView.setPadding(5, 0, 5, 0);
                textView.setBackgroundColor(Color.WHITE);
                textView.setTextColor(Color.BLACK);
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
        Populate(row);
    }

    public void Populate(IGridDataRow row)
    {
        IGridDataSource dataSource = row.DataSource();
        ArrayList<String> groupFields = new ArrayList<>(Arrays.asList(dataSource.GroupFields()));

        if (row.IsGroupRow())
        {
            TextView textView = _textViews[0];

            StringBuilder groupText = new StringBuilder();

            for (int i = 0; i < _gridColumns.size(); i++)
            {
                GridColumn gridColumn = _gridColumns.get(i);

                if (!groupFields.contains(gridColumn.FieldName()))
                {
                    continue;
                }

                if (groupText.length() != 0)
                {
                    groupText.append(", ");
                }

                //groupText.append(gridColumn.Caption());
                //groupText.append(" = ");
                groupText.append(row.Value(gridColumn.FieldName()).toString());
            }

            textView.setText(groupText.toString());
        }
        else
        {
            for (int i = 0; i < _gridColumns.size(); i++)
            {
                GridColumn gridColumn = _gridColumns.get(i);

                if (groupFields.contains(gridColumn.FieldName()))
                {
                    continue;
                }

                TextView textView = _textViews[i];

                textView.setWidth(gridColumn.Width()); //  * _scale);
                textView.setText(row.Value(gridColumn.FieldName()).toString());
            }
        }
    }
}
