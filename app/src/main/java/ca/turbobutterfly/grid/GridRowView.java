package ca.turbobutterfly.grid;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ca.turbobutterfly.data.IDataRow;

public class GridRowView extends LinearLayout
{
    TextView[] _textViews;
    Context _context;
    public ArrayList<GridColumn> _gridColumns;

    public GridRowView(Context context, ArrayList<GridColumn> gridColumns, IDataRow row)
    {
        super(context);
        _context = context;
        _gridColumns = gridColumns;

        setOrientation(HORIZONTAL);

        _textViews = new TextView[_gridColumns.size()];
        int cellSplitter = 0;

        for (int i = 0; i < _gridColumns.size(); i++)
        {
            TextView textView = new TextView(_context);
            _textViews[i] = textView;
            //textView.setTextSize(15);
            textView.setPadding(5, 0, 5, 0);
            textView.setBackgroundColor(Color.WHITE);
            textView.setTextColor(Color.BLACK);
            textView.setWidth(_gridColumns.get(i).Width()); //  * _scale);
            textView.setText(row.Value(_gridColumns.get(i).FieldName()));
            textView.setSingleLine(true);
            textView.setGravity(Gravity.CENTER_VERTICAL);

            addView(textView, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            if (cellSplitter < _gridColumns.size())
            {
                TextView splitterView = new TextView(_context);
                addView(splitterView, new LinearLayout.LayoutParams(
                        5,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                cellSplitter++;
            }
        }
        Populate(row);
    }

    public void Populate(IDataRow row)
    {
        for (int i = 0; i < row.ColumnCount(); i++)
        {
            _textViews[i].setWidth(_gridColumns.get(i).Width()); //  * _scale);
            _textViews[i].setText(row.Value(i));
        }
    }
}
