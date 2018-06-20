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
import ca.turbobutterfly.wachadoin.R;

public class GridRowView extends LinearLayout
{
    private final Context _context;
    private final List<GridColumn> _gridColumns;
    private final IGridDataRow _row;
    private final OnLongClickListener _onLongClickListener;
    private final OnClickListener _onClickListener;

    TextView[] _textViews;

    public GridRowView(
            Context context,
            ArrayList<GridColumn> gridColumns,
            IGridDataRow row,
            OnLongClickListener onLongClickListener,
            OnClickListener onClickListener)
    {
        super(context);

        _context = context;
        _gridColumns = gridColumns;
        _row = row;
        _onLongClickListener = onLongClickListener;
        _onClickListener = onClickListener;

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
            textView.setTag(R.string.grid_row_tag, _row);
            textView.setOnLongClickListener(_onLongClickListener);
            textView.setOnClickListener(_onClickListener);

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

                final TextView textView = new TextView(_context);
                _textViews[i] = textView;

                textView.setPadding(5, 0, 5, 0);
                textView.setBackgroundColor(Color.WHITE);
                textView.setTextColor(Color.BLACK);
                textView.setSingleLine(true);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setTag(R.string.grid_row_tag, _row);
                textView.setTag(R.string.grid_column_name_tag, gridColumn.FieldName());
                textView.setOnLongClickListener(_onLongClickListener);
                textView.setOnClickListener(_onClickListener);

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

    IGridDataRow Row()
    {
        return _row;
    }


    private void Populate(IGridDataRow row)
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
