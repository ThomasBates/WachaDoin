package ca.turbobutterfly.grid;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.support.v7.widget.AppCompatTextView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GridHeaderView extends LinearLayout
{
    TextView[] _textViews;
    Context _context;
    public ArrayList<GridColumn> _gridColumns;

    public GridHeaderView(Context context, ArrayList<GridColumn> gridColumns)
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
            //textView.setTextSize(16);
            textView.setPadding(5, 0, 5, 0);
            textView.setBackgroundColor(Color.LTGRAY);
            textView.setTextColor(Color.BLACK);
            textView.setWidth(_gridColumns.get(i).Width()); //  * _scale);
            textView.setText(_gridColumns.get(i).DisplayName());
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
    }
}
